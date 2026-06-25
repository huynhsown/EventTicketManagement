package com.ute.ticket.event.infrastructure.persistence.jpa.repository;

import com.ute.ticket.event.infrastructure.persistence.jpa.entity.TicketTypeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketTypeJpaRepository extends JpaRepository<TicketTypeJpaEntity, Long> {
    List<TicketTypeJpaEntity> findBySessionId(Long sessionId);
    Optional<TicketTypeJpaEntity> findBySessionIdAndNameIgnoreCase(Long sessionId, String name);
    boolean existsBySessionIdAndNameIgnoreCase(Long sessionId, String name);
}
