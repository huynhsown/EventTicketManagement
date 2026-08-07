package com.ute.ticket.event.infrastructure.persistence.adapter;

import com.ute.ticket.event.application.port.out.InventoryReservePort;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.InventoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class InventoryReserveAdapter implements InventoryReservePort {

    private final InventoryJpaRepository inventoryJpaRepository;

    @Override
    @Transactional
    public boolean reserve(Long ticketTypeId, int quantity) {
        return inventoryJpaRepository.reserveStock(ticketTypeId, quantity) == 1;
    }
}