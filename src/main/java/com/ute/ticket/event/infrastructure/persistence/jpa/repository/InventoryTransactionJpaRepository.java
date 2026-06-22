package com.ute.ticket.event.infrastructure.persistence.jpa.repository;

import com.ute.ticket.event.domain.enums.InventoryTransactionType;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.InventoryTransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryTransactionJpaRepository extends JpaRepository<InventoryTransactionJpaEntity, Long> {
    List<InventoryTransactionJpaEntity> findByTicketTypeId(Long ticketTypeId);
    List<InventoryTransactionJpaEntity> findByTicketTypeIdOrderByCreatedAtAsc(Long ticketTypeId);
    List<InventoryTransactionJpaEntity> findByReferenceId(UUID referenceId);
    List<InventoryTransactionJpaEntity> findByType(InventoryTransactionType type);
    List<InventoryTransactionJpaEntity> findByCreatedBy(String createdBy);
}
