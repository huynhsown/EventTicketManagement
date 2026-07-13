package com.ute.ticket.event.application.port.out;

import com.ute.ticket.event.domain.entity.InventoryTransaction;

public interface InventoryTransactionRepository {
    InventoryTransaction save(InventoryTransaction transaction);
}
