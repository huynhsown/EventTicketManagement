package com.ute.ticket.event.application.port.out;

import com.ute.ticket.event.domain.entity.TicketType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TicketTypeRepository {
    TicketType save(TicketType ticketType);
    Optional<TicketType> findActiveById(Long id);
    List<TicketType> findBySessionId(Long sessionId);
    List<TicketType> findBySessionIdsIn(Collection<Long> sessionIds);
    boolean existsBySessionIdAndNameIgnoreCase(Long sessionId, String name);
}
