package com.ute.ticket.event.application.result;

import java.time.Instant;

public record VenueSessionConflict(
        Long sessionId,
        Long eventId,
        String eventTitle,
        Instant startTime,
        Instant endTime
) {
}
