package com.ute.ticket.venue.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Builder
@Setter
@Schema(description = "Update venue request payload")
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVenueRequest {

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Venue name", example = "UTE Auditorium")
    private String name;

    @NotBlank
    @Schema(description = "Venue address", example = "1 Vo Van Ngan, Thu Duc")
    private String address;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Venue city", example = "Ho Chi Minh City")
    private String city;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Venue country", example = "Vietnam")
    private String country;

    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    @Schema(description = "Venue latitude", example = "10.8500000")
    private BigDecimal latitude;

    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    @Schema(description = "Venue longitude", example = "106.7700000")
    private BigDecimal longitude;

    @NotNull
    @Positive
    @Schema(description = "Venue capacity", example = "500")
    private Integer capacity;

    @Schema(description = "Venue description", example = "Main indoor auditorium")
    private String description;
}
