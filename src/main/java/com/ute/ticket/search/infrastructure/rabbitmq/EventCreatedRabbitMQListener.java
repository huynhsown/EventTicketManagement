package com.ute.ticket.search.infrastructure.rabbitmq;

import com.ute.ticket.event.application.port.out.CategoryRepository;
import com.ute.ticket.event.application.port.out.EventRepository;
import com.ute.ticket.event.domain.entity.Category;
import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.event.EventCreated;
import com.ute.ticket.organization.application.port.out.OrganizationRepository;
import com.ute.ticket.organization.domain.entity.Organization;
import com.ute.ticket.search.application.port.out.EventIndexer;
import com.ute.ticket.search.infrastructure.elasticsearch.document.EventDocument;
import com.ute.ticket.shared.config.RabbitMQConfig;
import com.ute.ticket.venue.application.port.out.VenueRepository;
import com.ute.ticket.venue.domain.entity.Venue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventCreatedRabbitMQListener {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final OrganizationRepository organizationRepository;
    private final VenueRepository venueRepository;
    private final EventIndexer eventIndexer;

    @RabbitListener(queues = RabbitMQConfig.EVENT_CREATED_QUEUE)
    public void onEventCreated(EventCreated eventCreated) {
        Long eventId = eventCreated.eventId();
        Set<Long> categoryIds = eventCreated.categoryIds();

        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            log.warn("Event [{}] not found for indexing, skipping", eventId);
            return;
        }

        eventIndexer.index(buildDocument(event, categoryIds));
        log.info("Indexed event [{}] into search", eventId);
    }

    private EventDocument buildDocument(Event event, Set<Long> categoryIds) {
        Organization organization = organizationRepository.findById(event.getOrganizationId()).orElse(null);
        Venue venue = event.getVenueId() != null ? venueRepository.findById(event.getVenueId()).orElse(null) : null;
        List<Category> categories = categoryIds != null && !categoryIds.isEmpty()
                ? categoryRepository.findByIdsIn(categoryIds)
                : List.of();

        return EventDocument.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .status(event.getStatus().name())
                .bannerUrl(event.getBannerUrl())
                .organizationId(event.getOrganizationId())
                .organizationName(organization != null ? organization.getName() : null)
                .venueId(venue != null ? venue.getId() : null)
                .venueName(venue != null ? venue.getName() : null)
                .venueCity(venue != null ? venue.getCity() : null)
                .categoryIds(categories.stream().map(Category::getId).toList())
                .categoryNames(categories.stream().map(Category::getName).toList())
                .categorySlugs(categories.stream().map(Category::getSlug).toList())
                .hasAvailableTickets(false)
                .publishedAt(event.getPublishedAt())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}
