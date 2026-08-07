package com.ute.ticket.event.application.port.out;

import com.ute.ticket.event.domain.entity.Inventory;
import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.domain.entity.TicketType;

import java.util.List;
import java.util.Map;

public interface CachePreSale {
    void warmUp(
            List<Session> sessions,
            Map<Long, List<TicketType>> ticketTypesBySession,
            Map<Long, Inventory> inventoryByTicketType
    );
}
