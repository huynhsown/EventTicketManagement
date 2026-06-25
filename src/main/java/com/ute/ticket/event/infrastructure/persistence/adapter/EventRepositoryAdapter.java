package com.ute.ticket.event.infrastructure.persistence.adapter;

import com.ute.ticket.event.application.port.out.EventRepository;
import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.EventJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.mapper.EventMapper;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.EventJpaRepository;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventRepositoryAdapter implements EventRepository {

    private final EventJpaRepository eventJpaRepository;
    private final EventMapper eventMapper;

    @Override
    public Event save(Event event) {
        EventJpaEntity jpaEntity;
        if (event.getId() == null || event.getVersion() == null) {
            jpaEntity = eventMapper.toJpaEntity(event);
        } else {
            jpaEntity = eventJpaRepository.findById(event.getId())
                    .orElseThrow(() -> new NotFoundException("Event not found"));
            eventMapper.updateEntity(jpaEntity, event);
        }
        EventJpaEntity saved = eventJpaRepository.save(jpaEntity);
        return eventMapper.toDomain(saved);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return eventJpaRepository.findById(id)
                .map(eventMapper::toDomain);
    }
}
