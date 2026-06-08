package com.ute.ticket.identity.infrastructure.keycloak;

import com.ute.ticket.identity.application.command.CreateUserCommand;
import com.ute.ticket.identity.application.port.out.AuthenticationProvider;
import com.ute.ticket.identity.application.port.out.IdentityProvider;
import com.ute.ticket.identity.application.result.LoginResult;
import com.ute.ticket.shared.config.KeycloakProperties;
import com.ute.ticket.shared.exception.UnauthorizedException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Override
    public LoginResult authenticate(String email, String password) {
        try (Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(properties.serverUrl())
                .realm(properties.app().realm())
                .clientId(properties.app().clientId())
                .clientSecret(properties.app().clientSecret())
                .username(email)
                .password(password)
                .grantType(OAuth2Constants.PASSWORD)
                .build()) {

            var token = keycloak.tokenManager().getAccessToken();
            return new LoginResult(
                    token.getToken(),
                    token.getRefreshToken(),
                    (long) token.getExpiresIn(),
                    token.getTokenType()
            );
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid username or password");
        }
    }
}

