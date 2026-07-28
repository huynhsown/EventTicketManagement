package com.ute.ticket.event.application.port.out;

public interface InventoryReservationPort {

    long decrease(Long sessionId, Long ticketTypeId, long quantity, long nowEpochSeconds);
}