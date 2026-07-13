package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.command.CreateTicketTypeCommand;
import com.ute.ticket.event.application.port.in.CreateTicketTypeUseCase;
import com.ute.ticket.event.application.port.out.EventRepository;
import com.ute.ticket.event.application.port.out.SessionRepository;
import com.ute.ticket.event.application.port.out.TicketTypeRepository;
import com.ute.ticket.event.application.result.TicketTypeResult;
import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.domain.entity.TicketType;
import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.shared.exception.ConflictException;
import com.ute.ticket.shared.exception.ForbiddenException;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateTicketTypeService implements CreateTicketTypeUseCase {

    private static final Set<SessionStatus> EDITABLE_SESSION_STATUSES =
            EnumSet.of(SessionStatus.SCHEDULED, SessionStatus.PUBLISHED, SessionStatus.HIDDEN);

    private final SessionRepository sessionRepository;
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    @Override
    public TicketTypeResult execute(CreateTicketTypeCommand cmd) {
        Session session = sessionRepository.findActiveById(cmd.getSessionId())
                .orElseThrow(() -> new NotFoundException("Session not found"));

        Event event = eventRepository.findActiveById(session.getEventId())
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (!organizationMemberRepository.existsAdminById(event.getOrganizationId(), cmd.getUserId())) {
            throw new ForbiddenException("Only organization owner or admin can create ticket types");
        }

        if (!EDITABLE_SESSION_STATUSES.contains(session.getStatus())) {
            throw new ConflictException("Ticket types can only be added while the session is SCHEDULED, PUBLISHED, or HIDDEN");
        }

        if (ticketTypeRepository.existsBySessionIdAndNameIgnoreCase(cmd.getSessionId(), cmd.getName())) {
            throw new ConflictException("Ticket type name already exists for this session");
        }

        TicketType ticketType = TicketType.create(
                cmd.getSessionId(),
                cmd.getName(),
                cmd.getDescription(),
                cmd.getPrice(),
                cmd.getMaxPerUser()
        );

        ticketType = ticketTypeRepository.save(ticketType);
        return TicketTypeResult.from(ticketType);
    }
}
