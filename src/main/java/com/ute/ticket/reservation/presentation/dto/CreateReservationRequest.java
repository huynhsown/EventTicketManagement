package com.ute.ticket.reservation.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@Schema(description = "Create reservation request payload")
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationRequest {

    @NotNull
    @Schema(description = "Ticket type id", example = "1")
    private Long ticketTypeId;

    @NotNull
    @Min(1)
    @Schema(description = "Number of tickets to reserve", example = "2")
    private Integer quantity;
}
