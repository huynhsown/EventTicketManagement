package com.ute.ticket.event.application.result;

import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.domain.enums.SessionStatus;

import java.time.Instant;

public record SessionResult(
        Long id,
        Long eventId,
        Instant startTime,
        Instant endTime,
        Instant salesStartAt,
        Instant salesEndAt,
        SessionStatus status
) {
    public static SessionResult from(Session session) {
        return new SessionResult(
                session.getId(),
                session.getEventId(),
                session.getStartTime(),
                session.getEndTime(),
                session.getSalesStartAt(),
                session.getSalesEndAt(),
                session.getStatus()
        );
    }
}
