package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.command.CreateEventCommand;
import com.ute.ticket.event.application.port.in.CreateEventUseCase;
import com.ute.ticket.event.application.port.out.CategoryRepository;
import com.ute.ticket.event.application.port.out.EventCategoryRepository;
import com.ute.ticket.event.application.port.out.EventRepository;
import com.ute.ticket.event.application.result.EventResult;
import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.entity.EventCategory;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.organization.application.port.out.OrganizationRepository;
import com.ute.ticket.shared.exception.BadRequestException;
import com.ute.ticket.shared.exception.ForbiddenException;
import com.ute.ticket.venue.application.port.out.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateEventService implements CreateEventUseCase {

    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final VenueRepository venueRepository;

    @Override
    public EventResult execute(CreateEventCommand cmd) {
        validateOrganization(cmd.getOrganizationId(), cmd.getUserId());
        validateVenue(cmd.getVenueId());
        validateCategories(cmd.getCategoryIds());

        Event event = eventRepository.save(Event.create(
                cmd.getOrganizationId(),
                cmd.getTitle(),
                cmd.getDescription(),
                cmd.getBannerUrl(),
                cmd.getVenueId()
        ));

        assignCategories(event.getId(), cmd.getCategoryIds());

        return EventResult.from(event);
    }

    private void validateOrganization(Long organizationId, Long userId) {
        if (!organizationRepository.existsActiveById(organizationId)) {
            throw new BadRequestException("Organization does not exist or is not active");
        }

        if (!organizationMemberRepository.existsAdminById(organizationId, userId)) {
            throw new ForbiddenException("Only organization owner or admin can create events");
        }
    }

    private void validateVenue(Long venueId) {
        if (venueId != null && !venueRepository.existsActiveById(venueId)) {
            throw new BadRequestException("Venue does not exist or is not active");
        }
    }

    private void validateCategories(Set<Long> categoryIds) {
        if (categoryIds != null
                && !categoryIds.isEmpty()
                && !categoryRepository.existsAllAssignable(categoryIds)) {
            throw new BadRequestException("One or more categories are invalid or inactive");
        }
    }

    private void assignCategories(Long eventId, Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }

        List<EventCategory> eventCategories = categoryIds.stream()
                .map(categoryId -> EventCategory.create(eventId, categoryId))
                .toList();
        eventCategoryRepository.saveAll(eventCategories);
    }
}
