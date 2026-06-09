package com.ute.ticket.identity.infrastructure.keycloak;

import com.ute.ticket.identity.application.command.CreateUserCommand;
import com.ute.ticket.identity.application.port.out.AuthenticationProvider;
import com.ute.ticket.identity.application.port.out.IdentityProvider;
import com.ute.ticket.identity.application.result.LoginResult;
import com.ute.ticket.shared.config.KeycloakProperties;
import com.ute.ticket.shared.exception.InternalException;
import com.ute.ticket.shared.exception.UnauthorizedException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakProviderImpl implements IdentityProvider, AuthenticationProvider {
    @Value("${keycloak.app.roles.user}")
    private String userRole;

    private final KeycloakProperties properties;

    @Override
    public String createUser(CreateUserCommand cmd) {
        try (Keycloak keycloak = loginAdmin()) {
            String realm = properties.app().realm();

            String userId = createKeycloakUser(keycloak, realm, cmd);
            setPassword(keycloak, realm, userId, cmd.getPassword());
            assignRealmRole(keycloak, realm, userId, userRole);

            return userId;
        }
    }

    @Override
    public void deleteUser(String userId) {
        try (Keycloak keycloak = loginAdmin()) {
            keycloak.realm(properties.app().realm())
                    .users()
                    .get(userId)
                    .remove();
        }
    }

    private String createKeycloakUser(Keycloak keycloak, String realm, CreateUserCommand cmd) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(cmd.getUsername());
        user.setEmail(cmd.getEmail());
        user.setFirstName(cmd.getFullName());
        user.setLastName(cmd.getFullName());
        user.setEmailVerified(true);

        Response response = keycloak.realm(realm)
                .users()
                .create(user);

        return CreatedResponseUtil.getCreatedId(response);
    }

    private void setPassword(Keycloak keycloak, String realm, String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false);
        credential.setValue(password);

        keycloak.realm(realm)
                .users()
                .get(userId)
                .resetPassword(credential);
    }

    private void assignRealmRole(Keycloak keycloak, String realm, String userId, String roleName) {
        RoleRepresentation role = keycloak.realm(realm)
                .roles()
                .get(roleName)
                .toRepresentation();

        keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(role));
    }

    private Keycloak loginAdmin() {
        return KeycloakBuilder.builder()
                .serverUrl(properties.serverUrl())
                .realm(properties.admin().realm())
                .clientId(properties.admin().clientId())
                .username(properties.admin().username())
                .password(properties.admin().password())
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }

    private RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private HttpEntity<MultiValueMap<String, String>> formRequest() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(headers);
    }

    private LoginResult doTokenRequest(MultiValueMap<String, String> body) {
        var response = restTemplate().postForEntity(
                tokenUrl(), new HttpEntity<>(body, formRequest().getHeaders()), AccessTokenResponse.class);
        var token = response.getBody();
        if (token == null) {
            throw new InternalException("Empty token response from Keycloak");
        }
        return new LoginResult(
                token.getToken(),
                token.getRefreshToken(),
                (long) token.getExpiresIn(),
                token.getTokenType()
        );
    }

    @Override
    public LoginResult authenticate(String email, String password) {
        try {
            var body = new LinkedMultiValueMap<String, String>();
            body.add("grant_type", "password");
            body.add("username", email);
            body.add("password", password);
            body.add("client_id", properties.app().clientId());
            body.add("client_secret", properties.app().clientSecret());
            return doTokenRequest(body);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    @Override
    public LoginResult refreshToken(String refreshToken) {
        try {
            var body = new LinkedMultiValueMap<String, String>();
            body.add("grant_type", "refresh_token");
            body.add("refresh_token", refreshToken);
            body.add("client_id", properties.app().clientId());
            body.add("client_secret", properties.app().clientSecret());
            return doTokenRequest(body);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid refresh token");
        }
    }

    @Override
    public void logout(String refreshToken) {
        try {
            var body = new LinkedMultiValueMap<String, String>();
            body.add("refresh_token", refreshToken);
            body.add("client_id", properties.app().clientId());
            body.add("client_secret", properties.app().clientSecret());

            restTemplate().postForEntity(
                    logoutUrl(), new HttpEntity<>(body, formRequest().getHeaders()), Void.class);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid refresh token");
        }
    }

    private String tokenUrl() {
        return properties.serverUrl() + "/realms/" + properties.app().realm() + "/protocol/openid-connect/token";
    }

    private String logoutUrl() {
        return properties.serverUrl() + "/realms/" + properties.app().realm() + "/protocol/openid-connect/logout";
    }
}

