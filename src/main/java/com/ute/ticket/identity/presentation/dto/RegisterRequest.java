package com.ute.ticket.identity.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@Setter
@Schema(description = "Registration request payload")
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Schema(description = "Unique username", example = "john_doe")
    private String username;

    @NotBlank
    @Email
    @Schema(description = "Email address", example = "john@example.com")
    private String email;

    @NotBlank
    @Schema(description = "Password", example = "P@ssw0rd")
    private String password;

    @NotBlank
    @Schema(description = "Password confirmation (must match password)", example = "P@ssw0rd")
    private String confirmPassword;

    @NotBlank
    @Schema(description = "Full name", example = "John Doe")
    private String fullName;

    @Schema(description = "Phone number", example = "+84123456789")
    private String phone;

    @Schema(description = "URL to avatar image", example = "https://example.com/avatar.jpg")
    private String avatarUrl;
}
