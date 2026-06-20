package com.ute.ticket.venue.presentation.dto;

import com.ute.ticket.venue.domain.enums.VenueStatus;
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
@Schema(description = "Change venue status request payload")
@NoArgsConstructor
@AllArgsConstructor
public class ChangeVenueStatusRequest {

    @NotNull
    @Schema(description = "Venue status", example = "ACTIVE")
    private VenueStatus status;
}
