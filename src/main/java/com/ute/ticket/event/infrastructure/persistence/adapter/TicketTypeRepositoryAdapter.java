package com.ute.ticket.event.infrastructure.persistence.adapter;

import com.ute.ticket.event.application.port.out.TicketTypeRepository;
import com.ute.ticket.event.domain.entity.TicketType;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.TicketTypeJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.mapper.TicketTypeMapper;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.TicketTypeJpaRepository;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TicketTypeRepositoryAdapter implements TicketTypeRepository {

    private final TicketTypeJpaRepository ticketTypeJpaRepository;
    private final TicketTypeMapper ticketTypeMapper;

    @Override
    public TicketType save(TicketType ticketType) {
        TicketTypeJpaEntity entity;
        if (ticketType.getId() == null || ticketType.getVersion() == null) {
            entity = ticketTypeMapper.toJpaEntity(ticketType);
        } else {
            entity = ticketTypeJpaRepository.findById(ticketType.getId())
                    .orElseThrow(() -> new NotFoundException("Ticket type not found"));
            ticketTypeMapper.updateEntity(entity, ticketType);
        }
        TicketTypeJpaEntity saved = ticketTypeJpaRepository.save(entity);
        return ticketTypeMapper.toDomain(saved);
    }

    @Override
    public Optional<TicketType> findActiveById(Long id) {
        return ticketTypeJpaRepository.findByIdAndDeletedAtIsNullAndStatus(id, TicketTypeStatus.ACTIVE)
                .map(ticketTypeMapper::toDomain);
    }

    @Override
    public List<TicketType> findBySessionId(Long sessionId) {
        return ticketTypeJpaRepository.findBySessionId(sessionId).stream()
                .map(ticketTypeMapper::toDomain)
                .toList();
    }

    @Override
    public List<TicketType> findBySessionIdsIn(Collection<Long> sessionIds) {
        return ticketTypeJpaRepository.findBySessionIdInAndStatus(
                        sessionIds,
                        TicketTypeStatus.ACTIVE
                ).stream()
                .map(ticketTypeMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsBySessionIdAndNameIgnoreCase(Long sessionId, String name) {
        return ticketTypeJpaRepository.existsBySessionIdAndNameIgnoreCase(sessionId, name);
    }
}
