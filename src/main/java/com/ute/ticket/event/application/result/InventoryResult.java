package com.ute.ticket.event.application.result;

import com.ute.ticket.event.domain.entity.Inventory;
import com.ute.ticket.event.domain.enums.InventoryStatus;

public record InventoryResult(
        Long ticketTypeId,
        int totalStock,
        int reservedStock,
        int soldStock,
        InventoryStatus status
) {
    public static InventoryResult from(Inventory inventory) {
        return new InventoryResult(
                inventory.getTicketTypeId(),
                inventory.getTotalStock(),
                inventory.getReservedStock(),
                inventory.getSoldStock(),
                inventory.getStatus()
        );
    }
}
