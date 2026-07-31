package com.ute.ticket.event.application.port.out;

public interface InventoryReleasePort {

    boolean release(Long ticketTypeId, int quantity);
}