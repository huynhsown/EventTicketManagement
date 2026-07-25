package com.ute.ticket.reservation.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Getter
@Builder
@Setter
@Schema(description = "Extend reservation timeout request payload")
@NoArgsConstructor
@AllArgsConstructor
public class ExtendReservationRequest {

    @NotNull
    @Min(1)
    @Schema(description = "Extension duration in minutes", example = "15")
    private Integer minutes;
}
