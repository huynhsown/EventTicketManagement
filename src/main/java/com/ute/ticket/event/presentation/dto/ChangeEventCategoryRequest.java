package com.ute.ticket.event.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Builder
@Setter
@Schema(description = "Change event categories request payload (full replacement)")
@NoArgsConstructor
@AllArgsConstructor
public class ChangeEventCategoryRequest {

    @Schema(description = "Full set of category ids to assign; empty/null removes all categories", example = "[\"1\", \"2\"]")
    private Set<Long> categoryIds;
}
