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
@Schema(description = "Create category request payload")
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {

    @NotBlank
    @Schema(description = "Category name", example = "Music")
    private String name;

    @Schema(description = "Unique slug (auto-generated from name if empty)", example = "music")
    private String slug;

    @Schema(description = "Category description", example = "Music concerts and live performances")
    private String description;

    @Schema(description = "Display order for sorting", example = "1")
    private Integer displayOrder;
}
