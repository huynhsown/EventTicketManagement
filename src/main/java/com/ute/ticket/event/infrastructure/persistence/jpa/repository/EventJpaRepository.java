package com.ute.ticket.event.infrastructure.persistence.jpa.repository;

import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.EventJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.InventoryJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.SessionJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.TicketTypeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventJpaRepository extends JpaRepository<EventJpaEntity, Long> {
    List<EventJpaEntity> findByOrganizationId(Long organizationId);
    List<EventJpaEntity> findByOrganizationIdAndStatus(Long organizationId, EventStatus status);
    List<EventJpaEntity> findByStatus(EventStatus status);
    Optional<EventJpaEntity> findByIdAndDeletedAtIsNull(Long eventId);
    boolean existsByOrganizationIdAndTitle(Long organizationId, String title);

    @Query("select coalesce(sum(i.totalStock), 0) " +
            "from InventoryJpaEntity i " +
            "join TicketTypeJpaEntity t on t.id = i.ticketTypeId " +
            "join SessionJpaEntity s on s.id = t.sessionId " +
            "where s.eventId = :eventId")
    long sumTotalStockByEventId(@Param("eventId") Long eventId);
}
