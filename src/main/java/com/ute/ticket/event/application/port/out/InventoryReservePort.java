package com.ute.ticket.event.application.port.out;

public interface InventoryReservePort {

    boolean reserve(Long ticketTypeId, int quantity);
}