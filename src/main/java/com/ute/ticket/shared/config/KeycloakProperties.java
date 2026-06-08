package com.ute.ticket.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
public record KeycloakProperties(
        String serverUrl,
        Admin admin,
        App app
) {

    public record Admin(
            String realm,
            String clientId,
            String username,
            String password
    ) {}

    public record App(
            String realm,
            String clientId,
            String clientSecret
    ) {}
}
