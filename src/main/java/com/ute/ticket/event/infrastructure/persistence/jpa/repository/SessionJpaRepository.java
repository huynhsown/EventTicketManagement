package com.ute.ticket.event.infrastructure.persistence.jpa.repository;

import com.ute.ticket.event.application.result.SessionRequiredCapacity;
import com.ute.ticket.event.application.result.VenueSessionConflict;
import com.ute.ticket.event.domain.enums.InventoryStatus;
import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.SessionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SessionJpaRepository extends JpaRepository<SessionJpaEntity, Long> {
    List<SessionJpaEntity> findByIdIn(Collection<Long> ids);
    List<SessionJpaEntity> findByEventId(Long eventId);
    List<SessionJpaEntity> findByEventIdAndStatus(Long eventId, SessionStatus status);
    List<SessionJpaEntity> findByStartTimeBetween(Instant start, Instant end);
    Optional<SessionJpaEntity> findByIdAndDeletedAtIsNull(Long id);

    @Query("""
        select count(s.id) > 0
        from SessionJpaEntity s
        where s.eventId = :eventId
            and s.status not in :statuses
            and s.startTime < :endTime
            and s.endTime > :startTime
            and s.deletedAt is null
    """)
    boolean existsSiblingOverlap(
            @Param("eventId") Long eventId,
            @Param("statuses") Collection<SessionStatus> statuses,
            @Param("endTime") Instant endTime,
            @Param("startTime") Instant startTime);

    @Query("""
            select new com.ute.ticket.event.application.result.VenueSessionConflict(
                s.id, e.id, e.title, s.startTime, s.endTime)
            from SessionJpaEntity s
            join EventJpaEntity e on e.id = s.eventId
            where e.venueId = :venueId
              and e.id <> :excludeEventId
              and e.status not in :terminalEventStatuses
              and s.status not in :terminalSessionStatuses
              and s.startTime < :endTime
              and s.endTime > :startTime
            """)
    List<VenueSessionConflict> findVenueOverlaps(
            @Param("venueId") Long venueId,
            @Param("excludeEventId") Long excludeEventId,
            @Param("terminalEventStatuses") Collection<com.ute.ticket.event.domain.enums.EventStatus> terminalEventStatuses,
            @Param("terminalSessionStatuses") Collection<SessionStatus> terminalSessionStatuses,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime);

    @Query("""
            select new com.ute.ticket.event.application.result.SessionRequiredCapacity(
                s.id, coalesce(sum(i.totalStock), 0))
            from SessionJpaEntity s
            left join TicketTypeJpaEntity t on t.sessionId = s.id
            left join InventoryJpaEntity i on i.ticketTypeId = t.id
            where s.eventId = :eventId
              and s.deletedAt is null
              and s.status not in :terminalSessionStatuses
            group by s.id
            """)
    List<SessionRequiredCapacity> findRequiredCapacitiesByEventId(
            @Param("eventId") Long eventId,
            @Param("terminalSessionStatuses") Collection<SessionStatus> terminalSessionStatuses);


    List<SessionJpaEntity> findBySalesStartAtBetweenAndStatus(
            Instant startTimeAfter,
            Instant startTimeBefore,
            SessionStatus sessionStatus
    );

    @Query("""
            select count(s.id) > 0
            from SessionJpaEntity s
            join TicketTypeJpaEntity t on t.sessionId = s.id
            join InventoryJpaEntity i on i.ticketTypeId = t.id
            where s.id = :sessionId
              and t.id = :ticketTypeId
              and s.deletedAt is null
              and s.status = :publishedStatus
              and s.salesStartAt <= :now
              and s.salesEndAt >= :now
              and i.status = :activeInventoryStatus
              and (i.totalStock - i.reservedStock - i.soldStock) >= :quantity
            """)
    boolean existsEligibleForPurchase(
            @Param("sessionId") Long sessionId,
            @Param("ticketTypeId") Long ticketTypeId,
            @Param("publishedStatus") SessionStatus publishedStatus,
            @Param("activeInventoryStatus") InventoryStatus activeInventoryStatus,
            @Param("now") Instant now,
            @Param("quantity") long quantity);
}
