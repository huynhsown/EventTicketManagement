package com.ute.ticket.event.application.port.out;

import com.ute.ticket.event.domain.entity.Inventory;

import java.util.Collection;
import java.util.List;

public interface InventoryRepository {
    Inventory save(Inventory inventory);
    boolean existsByTicketTypeId(Long ticketTypeId);
    List<Inventory> findByIdsIn(Collection<Long> ticketTypeIds);
}
