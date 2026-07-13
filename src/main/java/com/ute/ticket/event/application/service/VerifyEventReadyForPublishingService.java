package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.command.VerifyEventReadyForPublishingCommand;
import com.ute.ticket.event.application.port.in.VerifyEventReadyForPublishingUseCase;
import com.ute.ticket.event.application.port.out.EventRepository;
import com.ute.ticket.event.application.port.out.InventoryRepository;
import com.ute.ticket.event.application.port.out.SessionRepository;
import com.ute.ticket.event.application.port.out.TicketTypeRepository;
import com.ute.ticket.event.application.result.EventReadinessResult;
import com.ute.ticket.event.application.result.EventReadinessResult.ReadinessCheck;
import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.domain.entity.TicketType;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.shared.exception.ForbiddenException;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VerifyEventReadyForPublishingService implements VerifyEventReadyForPublishingUseCase {

    private final EventRepository eventRepository;
    private final SessionRepository sessionRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final InventoryRepository inventoryRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    @Override
    public EventReadinessResult execute(VerifyEventReadyForPublishingCommand cmd) {
        Event event = eventRepository.findActiveById(cmd.getEventId())
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (!organizationMemberRepository.existsById(event.getOrganizationId(), cmd.getUserId())) {
            throw new ForbiddenException("Only organization members can view event readiness");
        }

        List<Session> sessions = sessionRepository.findByEventId(event.getId()).stream()
                .filter(session -> !session.isTerminal())
                .toList();

        List<ReadinessCheck> checks = List.of(
                venueCheck(event),
                sessionCheck(sessions),
                ticketTypeCheck(sessions)
        );

        return EventReadinessResult.of(event.getId(), checks);
    }

    private ReadinessCheck venueCheck(Event event) {
        if (event.getVenueId() != null) {
            return new ReadinessCheck("venue", true, "Venue is assigned");
        }
        return new ReadinessCheck("venue", false, "Venue is not assigned");
    }

    private ReadinessCheck sessionCheck(List<Session> sessions) {
        if (!sessions.isEmpty()) {
            return new ReadinessCheck("sessions", true, "At least one session exists");
        }
        return new ReadinessCheck("sessions", false, "No sessions found");
    }

    private ReadinessCheck ticketTypeCheck(List<Session> sessions) {
        if (sessions.isEmpty()) {
            return new ReadinessCheck("ticket-types", false, "No sessions to evaluate");
        }

        List<Long> failingSessions = new ArrayList<>();
        for (Session session : sessions) {
            boolean hasReadyTicketType = ticketTypeRepository.findBySessionId(session.getId()).stream()
                    .filter(ticketType -> ticketType.getStatus() == TicketTypeStatus.ACTIVE)
                    .anyMatch(ticketType -> inventoryRepository.existsByTicketTypeId(ticketType.getId()));

            if (!hasReadyTicketType) {
                failingSessions.add(session.getId());
            }
        }

        if (failingSessions.isEmpty()) {
            return new ReadinessCheck(
                    "ticket-types",
                    true,
                    "Every session has an active ticket type with initialized inventory"
            );
        }
        return new ReadinessCheck(
                "ticket-types",
                false,
                "Missing active ticket type with initialized inventory for session(s): " + failingSessions
        );
    }
}
