package com.ute.ticket.event.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@Schema(description = "Assign venue to event request payload")
@NoArgsConstructor
@AllArgsConstructor
public class AssignVenueRequest {

    @NotNull
    @Schema(description = "Venue id to assign (must be active)", example = "1")
    private Long venueId;
}
