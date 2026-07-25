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
@Schema(description = "Change reservation quantity request payload")
@NoArgsConstructor
@AllArgsConstructor
public class ChangeReservationQuantityRequest {

    @NotNull
    @Min(1)
    @Schema(description = "New number of tickets", example = "3")
    private Integer quantity;
}
