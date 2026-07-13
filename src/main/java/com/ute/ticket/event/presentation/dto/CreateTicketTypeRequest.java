package com.ute.ticket.event.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Create ticket type request payload")
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketTypeRequest {

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Ticket type name (unique per session, case-insensitive)", example = "VIP")
    private String name;

    @Size(max = 4000)
    @Schema(description = "Ticket type description", example = "VIP access with backstage tour")
    private String description;

    @NotNull
    @DecimalMin(value = "0.0")
    @Digits(integer = 6, fraction = 2)
    @Schema(description = "Ticket price (0 to 999999.99)", example = "150.00")
    private BigDecimal price;

    @NotNull
    @Min(1)
    @Max(100)
    @Schema(description = "Max tickets a single user can buy", example = "4")
    private Integer maxPerUser;
}
