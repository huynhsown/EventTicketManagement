package com.ute.ticket.event.application.port.out;

import com.ute.ticket.event.domain.entity.Inventory;

public interface InventoryRepository {
    Inventory save(Inventory inventory);
    boolean existsByTicketTypeId(Long ticketTypeId);
}
