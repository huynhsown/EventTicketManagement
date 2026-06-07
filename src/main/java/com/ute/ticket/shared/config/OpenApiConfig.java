package com.ute.ticket.shared.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Event Ticket API", version = "1.0", description = "API for event ticket management system"),
        servers = @Server(url = "http://localhost:8090", description = "Local server")
)
public class OpenApiConfig {
}
