package com.ute.ticket.identity.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Login request payload")
public class LoginRequest {

    @NotBlank
    @Schema(description = "Email", example = "john_doe@mm.com")
    private String email;

    @NotBlank
    @Schema(description = "Password", example = "P@ssw0rd")
    private String password;
}
