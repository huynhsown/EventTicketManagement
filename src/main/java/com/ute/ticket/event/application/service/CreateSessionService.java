package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.command.CreateSessionCommand;
import com.ute.ticket.event.application.port.in.CreateSessionUseCase;
import com.ute.ticket.event.application.port.out.EventRepository;
import com.ute.ticket.event.application.port.out.SessionRepository;
import com.ute.ticket.event.application.result.SessionResult;
import com.ute.ticket.event.application.result.VenueSessionConflict;
import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.shared.exception.BadRequestException;
import com.ute.ticket.shared.exception.ConflictException;
import com.ute.ticket.shared.exception.ForbiddenException;
import com.ute.ticket.shared.exception.NotFoundException;
import com.ute.ticket.venue.application.port.out.VenueRepository;
import com.ute.ticket.venue.domain.entity.Venue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateSessionService implements CreateSessionUseCase {

    private final EventRepository eventRepository;
    private final SessionRepository sessionRepository;
    private final VenueRepository venueRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    @Override
    public SessionResult execute(CreateSessionCommand cmd) {
        Event event = eventRepository.findActiveById(cmd.getEventId())
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (!organizationMemberRepository.existsAdminById(event.getOrganizationId(), cmd.getUserId())) {
            throw new ForbiddenException("Only organization owner or admin can create sessions");
        }

        if (event.getStatus() != EventStatus.DRAFT) {
            throw new ConflictException("Add sessions before publishing");
        }

        Long effectiveVenueId = event.getVenueId();
        if (effectiveVenueId == null) {
            throw new BadRequestException("Assign a venue to the event first");
        }
        validateVenue(effectiveVenueId);

        Session session = Session.create(
                event.getId(),
                cmd.getStartTime(),
                cmd.getEndTime(),
                cmd.getSalesStartAt(),
                cmd.getSalesEndAt()
        );

        validateNoOverlaps(event.getId(), effectiveVenueId, session.getStartTime(), session.getEndTime());

        session = sessionRepository.save(session);
        return SessionResult.from(session);
    }

    private void validateNoOverlaps(Long eventId, Long venueId, Instant startTime, Instant endTime) {
        if (sessionRepository.existsSiblingOverlap(eventId, startTime, endTime)) {
            throw new ConflictException("Session overlaps with an existing session of this event");
        }

        List<VenueSessionConflict> venueOverlaps =
                sessionRepository.findVenueOverlaps(venueId, eventId, startTime, endTime);
        if (!venueOverlaps.isEmpty()) {
            throw new ConflictException("Session overlaps with another event's session at the same venue");
        }
    }

    private void validateVenue(Long venueId) {
        Venue venue = venueRepository.findActiveById(venueId)
                .orElseThrow(() -> new BadRequestException("Venue does not exist or is not active"));

        if (!venue.isActive()) {
            throw new BadRequestException("Venue does not exist or is not active");
        }
    }
}
