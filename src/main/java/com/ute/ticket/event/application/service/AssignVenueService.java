package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.command.AssignVenueCommand;
import com.ute.ticket.event.application.port.in.AssignVenueUseCase;
import com.ute.ticket.event.application.port.out.EventRepository;
import com.ute.ticket.event.application.result.EventResult;
import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.event.EventVenueAssigned;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.shared.application.event.EventPublisher;
import com.ute.ticket.shared.exception.BadRequestException;
import com.ute.ticket.shared.exception.ConflictException;
import com.ute.ticket.shared.exception.ForbiddenException;
import com.ute.ticket.shared.exception.NotFoundException;
import com.ute.ticket.venue.application.port.out.VenueRepository;
import com.ute.ticket.venue.domain.entity.Venue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignVenueService implements AssignVenueUseCase {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final EventPublisher eventPublisher;

    @Override
    public EventResult execute(AssignVenueCommand cmd) {
        Event event = eventRepository.findActiveById(cmd.getEventId())
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (!organizationMemberRepository.existsAdminById(event.getOrganizationId(), cmd.getUserId())) {
            throw new ForbiddenException("Only organization owner or admin can assign a venue");
        }

        if (event.getVenueId() != null) {
            throw new ConflictException("Event already has a venue assigned. Use Change Venue to assign a different venue.");
        }

        Venue venue = venueRepository.findActiveById(cmd.getVenueId())
                .orElseThrow(() -> new BadRequestException("Venue does not exist or is not active"));

        if (!venue.isActive()) {
            throw new BadRequestException("Venue does not exist or is not active");
        }

        event.assignVenue(venue.getId());
        event = eventRepository.save(event);

        eventPublisher.publish(new EventVenueAssigned(event.getId(), venue.getId()));

        return EventResult.from(event);
    }
}
