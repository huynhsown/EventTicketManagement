package com.ute.ticket.organization.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@Schema(description = "Change organization slug request payload")
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOrganizationSlugRequest {

    @NotBlank
    @Pattern(
            regexp = "^[a-z0-9]+(-[a-z0-9]+)*$",
            message = "Slug must be lowercase alphanumeric words separated by single hyphens"
    )
    @Schema(description = "New unique slug", example = "ute-events")
    private String newSlug;
}
