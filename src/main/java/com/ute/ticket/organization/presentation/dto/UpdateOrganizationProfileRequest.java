package com.ute.ticket.organization.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Builder
@Setter
@Schema(description = "Update organization profile request payload")
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrganizationProfileRequest {

    @NotBlank
    @Schema(description = "Organization name", example = "UTE Events")
    private String name;

    @Schema(description = "Organization description", example = "University of Technology and Engineering events hub")
    private String description;

    @URL
    @Schema(description = "URL to organization logo", example = "https://example.com/logo.png")
    private String logoUrl;

    @URL
    @Schema(description = "Organization website", example = "https://ute.edu.vn")
    private String website;
}
