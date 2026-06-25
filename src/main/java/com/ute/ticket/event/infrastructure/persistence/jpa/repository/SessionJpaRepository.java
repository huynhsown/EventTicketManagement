package com.ute.ticket.event.infrastructure.persistence.jpa.repository;

import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.SessionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface SessionJpaRepository extends JpaRepository<SessionJpaEntity, Long> {
    List<SessionJpaEntity> findByEventId(Long eventId);
    List<SessionJpaEntity> findByEventIdAndStatus(Long eventId, SessionStatus status);
    List<SessionJpaEntity> findByStartTimeBetween(Instant start, Instant end);
}
