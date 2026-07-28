package com.ute.ticket.event.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
@Schema(description = "Publish sessions batch request payload")
@NoArgsConstructor
@AllArgsConstructor
public class PublishSessionsRequest {

    @NotEmpty
    @Schema(description = "Session ids to publish", example = "[1, 2, 3]")
    private List<Long> sessionIds;
}