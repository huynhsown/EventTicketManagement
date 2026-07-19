package com.ute.ticket.event.infrastructure.persistence.adapter;

import com.ute.ticket.event.application.port.out.InventoryRepository;
import com.ute.ticket.event.domain.entity.Inventory;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.InventoryJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.mapper.InventoryMapper;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.InventoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InventoryRepositoryAdapter implements InventoryRepository {

    private final InventoryJpaRepository inventoryJpaRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public Inventory save(Inventory inventory) {
        InventoryJpaEntity entity = inventoryMapper.toJpaEntity(inventory);
        InventoryJpaEntity saved = inventoryJpaRepository.save(entity);
        return inventoryMapper.toDomain(saved);
    }

    @Override
    public boolean existsByTicketTypeId(Long ticketTypeId) {
        return inventoryJpaRepository.existsById(ticketTypeId);
    }

    @Override
    public List<Inventory> findByIdsIn(Collection<Long> ticketTypeIds) {
        return inventoryJpaRepository.findByTicketTypeIdIn(ticketTypeIds).stream()
                .map(inventoryMapper::toDomain)
                .toList();
    }
}
