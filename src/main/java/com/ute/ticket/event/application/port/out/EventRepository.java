package com.ute.ticket.event.application.port.out;

import com.ute.ticket.event.domain.entity.Event;

import java.util.Optional;

public interface EventRepository {
    Event save(Event event);
    Optional<Event> findById(Long id);
}
