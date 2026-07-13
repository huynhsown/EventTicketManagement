package com.ute.ticket.event.application.port.out;

import com.ute.ticket.event.application.result.SessionRequiredCapacity;
import com.ute.ticket.event.application.result.VenueSessionConflict;
import com.ute.ticket.event.domain.entity.Session;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SessionRepository {
    Session save(Session session);
    Optional<Session> findById(Long id);
    Optional<Session> findActiveById(Long id);
    List<Session> findByEventId(Long eventId);
    boolean existsSiblingOverlap(Long eventId, Instant startTime, Instant endTime);
    List<VenueSessionConflict> findVenueOverlaps(Long venueId, Long excludeEventId, Instant startTime, Instant endTime);
    List<SessionRequiredCapacity> findRequiredCapacitiesByEventId(Long eventId);
}
