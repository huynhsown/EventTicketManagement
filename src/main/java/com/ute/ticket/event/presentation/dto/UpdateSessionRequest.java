package com.ute.ticket.event.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Builder
@Setter
@Schema(description = "Update session sales window request payload")
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSessionRequest {

    @Schema(description = "Sales window start (optional)", example = "2026-08-01T00:00:00Z")
    private Instant salesStartAt;

    @Schema(description = "Sales window end, must be <= session start (optional)", example = "2026-08-31T23:59:59Z")
    private Instant salesEndAt;
}
