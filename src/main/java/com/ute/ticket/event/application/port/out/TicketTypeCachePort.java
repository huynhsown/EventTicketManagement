package com.ute.ticket.event.application.port.out;

import com.ute.ticket.event.domain.entity.TicketType;

import java.util.Optional;

public interface TicketTypeCachePort {

    Optional<TicketType> findActiveById(Long id);
}