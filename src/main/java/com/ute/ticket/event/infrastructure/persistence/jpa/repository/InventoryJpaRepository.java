package com.ute.ticket.event.infrastructure.persistence.jpa.repository;

import com.ute.ticket.event.domain.enums.InventoryStatus;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.InventoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InventoryJpaRepository extends JpaRepository<InventoryJpaEntity, Long> {
    Optional<InventoryJpaEntity> findByTicketTypeId(Long ticketTypeId);
    List<InventoryJpaEntity> findByTicketTypeIdIn(Collection<Long> ticketTypeIds);
    List<InventoryJpaEntity> findByTicketTypeIdInAndStatus(Collection<Long> ticketTypeIds, InventoryStatus status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
            UPDATE inventories
            SET reserved_stock = reserved_stock + :quantity,
                updated_at = NOW()
            WHERE ticket_type_id = :ticketTypeId
              AND status = 'ACTIVE'
              AND total_stock - reserved_stock - sold_stock >= :quantity
            """, nativeQuery = true)
    int reserveStock(@Param("ticketTypeId") Long ticketTypeId, @Param("quantity") int quantity);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
            UPDATE inventories
            SET reserved_stock = GREATEST(reserved_stock - :quantity, 0),
                updated_at = NOW()
            WHERE ticket_type_id = :ticketTypeId
              AND status = 'ACTIVE'
              AND reserved_stock >= :quantity
            """, nativeQuery = true)
    int releaseStock(@Param("ticketTypeId") Long ticketTypeId, @Param("quantity") int quantity);
}
