package com.ute.ticket.event.infrastructure.persistence.jpa.repository;

import com.ute.ticket.event.infrastructure.persistence.jpa.entity.InventoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InventoryJpaRepository extends JpaRepository<InventoryJpaEntity, Long> {
    Optional<InventoryJpaEntity> findByTicketTypeId(Long ticketTypeId);
    List<InventoryJpaEntity> findByTicketTypeIdIn(Collection<Long> ticketTypeIds);
}
