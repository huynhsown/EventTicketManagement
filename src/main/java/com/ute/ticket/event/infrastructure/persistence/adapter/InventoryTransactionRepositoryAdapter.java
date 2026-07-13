package com.ute.ticket.event.infrastructure.persistence.adapter;

import com.ute.ticket.event.application.port.out.InventoryTransactionRepository;
import com.ute.ticket.event.domain.entity.InventoryTransaction;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.InventoryTransactionJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.mapper.InventoryTransactionMapper;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.InventoryTransactionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InventoryTransactionRepositoryAdapter implements InventoryTransactionRepository {

    private final InventoryTransactionJpaRepository inventoryTransactionJpaRepository;
    private final InventoryTransactionMapper inventoryTransactionMapper;

    @Override
    public InventoryTransaction save(InventoryTransaction transaction) {
        InventoryTransactionJpaEntity entity = inventoryTransactionMapper.toJpaEntity(transaction);
        InventoryTransactionJpaEntity saved = inventoryTransactionJpaRepository.save(entity);
        return inventoryTransactionMapper.toDomain(saved);
    }
}
