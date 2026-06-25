package com.ute.ticket.event.presentation.dto;

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
@Schema(description = "Rename category request payload")
@NoArgsConstructor
@AllArgsConstructor
public class RenameCategoryRequest {

    @NotBlank
    @Schema(description = "New category name", example = "Live Music")
    private String name;

    @Schema(description = "New unique slug (auto-generated from name if empty)", example = "live-music")
    private String slug;
}
