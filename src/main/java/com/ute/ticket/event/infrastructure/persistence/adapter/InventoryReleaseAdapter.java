package com.ute.ticket.event.infrastructure.persistence.adapter;

import com.ute.ticket.event.application.port.out.InventoryReleasePort;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.InventoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class InventoryReleaseAdapter implements InventoryReleasePort {

    private final InventoryJpaRepository inventoryJpaRepository;

    @Override
    @Transactional
    public boolean release(Long ticketTypeId, int quantity) {
        return inventoryJpaRepository.releaseStock(ticketTypeId, quantity) == 1;
    }
}