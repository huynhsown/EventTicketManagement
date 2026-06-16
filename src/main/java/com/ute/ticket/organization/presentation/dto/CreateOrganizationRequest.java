package com.ute.ticket.organization.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@Schema(description = "Create organization request payload")
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrganizationRequest {

    @NotBlank
    @Schema(description = "Organization name", example = "UTE Events")
    private String name;

    @Schema(description = "Unique slug (auto-generated from name if empty)", example = "ute-events")
    private String slug;

    @Schema(description = "Organization description", example = "University of Technology and Engineering events hub")
    private String description;

    @Schema(description = "URL to organization logo", example = "https://example.com/logo.png")
    private String logoUrl;

    @Schema(description = "Organization website", example = "https://ute.edu.vn")
    private String website;
}
