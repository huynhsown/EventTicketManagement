package com.ute.ticket.event.presentation.dto;

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
@Schema(description = "Initialize inventory request payload")
@NoArgsConstructor
@AllArgsConstructor
public class InitializeInventoryRequest {

    @NotNull
    @Min(1)
    @Schema(description = "Initial stock quantity (must be greater than zero)", example = "100")
    private Integer quantity;
}
