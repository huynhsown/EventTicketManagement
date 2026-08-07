package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.port.in.PreSalePreloadUseCase;
import com.ute.ticket.event.application.port.out.*;
import com.ute.ticket.event.domain.entity.Inventory;
import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.domain.entity.TicketType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreSalePreloadService implements PreSalePreloadUseCase {
    private final SessionRepository sessionRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final InventoryRepository inventoryRepository;
    private final CachePreSale cachePreSale;

    @Override
    public void execute() {
        List<Session> sessions = sessionRepository.findSessionsStartingSaleWithin(30);
        Set<Long> sessionIds = sessions.stream()
                .map(Session::getId)
                .collect(Collectors.toSet());

        List<TicketType> ticketTypes = ticketTypeRepository.findBySessionIdsIn(sessionIds);
        Map<Long, List<TicketType>> ticketTypesBySession = ticketTypes.stream()
                .collect(Collectors.groupingBy(TicketType::getSessionId));
        Set<Long> ticketTypeIds = ticketTypes.stream()
                .map(TicketType::getId)
                .collect(Collectors.toSet());

        List<Inventory> inventories = inventoryRepository.findByIdsIn(ticketTypeIds);
        Map<Long, Inventory> inventoryByTicketType = inventories.stream()
                .collect(Collectors.toMap(Inventory::getTicketTypeId, i -> i));

        cachePreSale.warmUp(sessions, ticketTypesBySession, inventoryByTicketType);
    }
}
