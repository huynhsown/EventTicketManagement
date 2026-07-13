package com.ute.ticket.event.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Builder
@Setter
@Schema(description = "Create session request payload")
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionRequest {

    @NotNull
    @Schema(description = "Session start time", example = "2026-09-01T09:00:00Z")
    private Instant startTime;

    @NotNull
    @Schema(description = "Session end time (must be after startTime)", example = "2026-09-01T12:00:00Z")
    private Instant endTime;

    @NotNull
    @Schema(description = "Sales window start", example = "2026-08-01T00:00:00Z")
    private Instant salesStartAt;

    @NotNull
    @Schema(description = "Sales window end (must be <= startTime)", example = "2026-08-31T23:59:59Z")
    private Instant salesEndAt;
}
