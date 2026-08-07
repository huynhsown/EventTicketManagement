package com.ute.ticket.event.infrastructure.persistence.adapter;

import com.ute.ticket.event.application.port.out.SessionRepository;
import com.ute.ticket.event.application.result.SessionRequiredCapacity;
import com.ute.ticket.event.application.result.VenueSessionConflict;
import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.SessionJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.mapper.SessionMapper;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.SessionJpaRepository;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SessionRepositoryAdapter implements SessionRepository {

    private static final List<EventStatus> TERMINAL_EVENT_STATUSES =
            List.of(EventStatus.CANCELLED, EventStatus.ARCHIVED);

    private static final List<SessionStatus> TERMINAL_SESSION_STATUSES =
            List.of(SessionStatus.CANCELLED, SessionStatus.DELETED);

    private final SessionJpaRepository sessionJpaRepository;
    private final SessionMapper sessionMapper;

    @Override
    public Session save(Session session) {
        SessionJpaEntity entity;
        if (session.getId() == null || session.getVersion() == null) {
            entity = sessionMapper.toJpaEntity(session);
        } else {
            entity = sessionJpaRepository.findById(session.getId())
                    .orElseThrow(() -> new NotFoundException("Session not found"));
            sessionMapper.updateEntity(entity, session);
        }
        SessionJpaEntity saved = sessionJpaRepository.save(entity);
        return sessionMapper.toDomain(saved);
    }

    @Override
    public List<Session> saveAll(List<Session> sessions) {
        List<SessionJpaEntity> entities = sessions.stream()
                .map(session -> {
                    if (session.getId() == null || session.getVersion() == null) {
                        return sessionMapper.toJpaEntity(session);
                    }
                    SessionJpaEntity entity = sessionJpaRepository.findById(session.getId())
                            .orElseThrow(() -> new NotFoundException("Session not found"));
                    sessionMapper.updateEntity(entity, session);
                    return entity;
                })
                .toList();
        return sessionJpaRepository.saveAll(entities).stream()
                .map(sessionMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Session> findById(Long id) {
        return sessionJpaRepository.findById(id)
                .map(sessionMapper::toDomain);
    }

    @Override
    public Optional<Session> findActiveById(Long id) {
        return sessionJpaRepository.findByIdAndDeletedAtIsNull(id)
                .map(sessionMapper::toDomain);
    }

    @Override
    public List<Session> findByIds(Collection<Long> ids) {
        return sessionJpaRepository.findByIdIn(ids).stream()
                .map(sessionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Session> findByEventId(Long eventId) {
        return sessionJpaRepository.findByEventId(eventId).stream()
                .map(sessionMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsSiblingOverlap(Long eventId, Instant startTime, Instant endTime) {
        return sessionJpaRepository.existsSiblingOverlap(
                eventId,
                TERMINAL_SESSION_STATUSES,
                endTime,
                startTime
        );
    }

    @Override
    public List<VenueSessionConflict> findVenueOverlaps(
            Long venueId,
            Long excludeEventId,
            Instant startTime,
            Instant endTime
    ) {
        return sessionJpaRepository.findVenueOverlaps(
                venueId,
                excludeEventId,
                TERMINAL_EVENT_STATUSES,
                TERMINAL_SESSION_STATUSES,
                startTime,
                endTime
        );
    }

    @Override
    public List<SessionRequiredCapacity> findRequiredCapacitiesByEventId(Long eventId) {
        return sessionJpaRepository.findRequiredCapacitiesByEventId(
                eventId,
                TERMINAL_SESSION_STATUSES
        );
    }

    @Override
    public List<Session> findSessionsStartingSaleWithin(Integer time) {
        Instant now = Instant.now();
        Instant upperBound = now.plus(time, ChronoUnit.MINUTES);
        List<SessionJpaEntity> jpaEntities = sessionJpaRepository
                .findBySalesStartAtBetweenAndStatus(now, upperBound, SessionStatus.PUBLISHED);
        return jpaEntities.stream()
                .map(sessionMapper::toDomain)
                .toList();
    }
}
