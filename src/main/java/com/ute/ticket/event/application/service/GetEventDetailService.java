package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.port.in.GetEventDetailUseCase;
import com.ute.ticket.event.application.port.out.*;
import com.ute.ticket.event.application.result.EventDetailResult;
import com.ute.ticket.event.application.result.EventDetailResult.CategoryRef;
import com.ute.ticket.event.application.result.EventDetailResult.InventoryInfo;
import com.ute.ticket.event.application.result.EventDetailResult.OrganizationRef;
import com.ute.ticket.event.application.result.EventDetailResult.SessionDetail;
import com.ute.ticket.event.application.result.EventDetailResult.TicketTypeDetail;
import com.ute.ticket.event.application.result.EventDetailResult.VenueRef;
import com.ute.ticket.event.domain.entity.Category;
import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.entity.EventCategory;
import com.ute.ticket.event.domain.entity.Inventory;
import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.domain.entity.TicketType;
import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;
import com.ute.ticket.organization.application.port.out.OrganizationRepository;
import com.ute.ticket.organization.domain.entity.Organization;
import com.ute.ticket.shared.exception.NotFoundException;
import com.ute.ticket.venue.application.port.out.VenueRepository;
import com.ute.ticket.venue.domain.entity.Venue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetEventDetailService implements GetEventDetailUseCase {

    private static final Set<EventStatus> PUBLIC_STATUSES =
            EnumSet.of(EventStatus.PUBLISHED, EventStatus.LIVE, EventStatus.SALES_PAUSED);
    private static final Set<SessionStatus> ON_SALE_SESSION_STATUSES =
            EnumSet.of(SessionStatus.SCHEDULED, SessionStatus.PUBLISHED, SessionStatus.LIVE);

    private final EventRepository eventRepository;
    private final SessionRepository sessionRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final InventoryRepository inventoryRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final OrganizationRepository organizationRepository;
    private final VenueRepository venueRepository;

    private final EventCachePort eventCachePort;

    @Override
    public EventDetailResult execute(String slug) {

        EventDetailResult cached = eventCachePort.findBySlug(slug);
        if (cached != null) {
            return cached;
        }

        Event event = eventRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (!PUBLIC_STATUSES.contains(event.getStatus())) {
            throw new NotFoundException("Event not found");
        }

        EventDetailResult result = buildDetail(event);
        eventCachePort.save(result);
        return result;
    }

    private EventDetailResult buildDetail(Event event) {
        Organization organization = organizationRepository.findById(event.getOrganizationId()).orElse(null);
        Venue venue = event.getVenueId() != null ? venueRepository.findById(event.getVenueId()).orElse(null) : null;

        List<Category> categories = findCategories(event.getId());
        List<SessionDetail> sessions = buildSessions(event.getId());

        return new EventDetailResult(
                event.getId(),
                event.getSlug(),
                event.getTitle(),
                event.getDescription(),
                event.getStatus(),
                event.getStatus() == EventStatus.SALES_PAUSED,
                event.getBannerUrl(),
                organization != null
                        ? new OrganizationRef(organization.getId(), organization.getName(), organization.getLogoUrl(), organization.getSlug())
                        : null,
                venue != null
                        ? new VenueRef(venue.getId(), venue.getName(), venue.getAddress(), venue.getCity())
                        : null,
                categories.stream()
                        .sorted(Comparator.comparing(Category::getId))
                        .map(category -> new CategoryRef(category.getId(), category.getName(), category.getSlug()))
                        .toList(),
                sessions,
                event.getPublishedAt(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }

    private List<Category> findCategories(Long eventId) {
        List<EventCategory> links = eventCategoryRepository.findByEventId(eventId);
        if (links.isEmpty()) {
            return List.of();
        }
        List<Long> categoryIds = links.stream().map(EventCategory::getCategoryId).toList();
        return categoryRepository.findByIdsIn(categoryIds);
    }

    private List<SessionDetail> buildSessions(Long eventId) {
        List<Session> sessions = sessionRepository.findByEventId(eventId).stream()
                .filter(session -> ON_SALE_SESSION_STATUSES.contains(session.getStatus()))
                .filter(session -> !session.isDeleted())
                .sorted(Comparator.comparing(Session::getStartTime))
                .toList();

        if (sessions.isEmpty()) {
            return List.of();
        }

        List<Long> sessionIds = sessions.stream().map(Session::getId).toList();
        List<TicketType> ticketTypes = ticketTypeRepository.findBySessionIdsIn(sessionIds).stream()
                .filter(ticketType -> ticketType.getStatus() == TicketTypeStatus.ACTIVE)
                .filter(ticketType -> !ticketType.isDeleted())
                .sorted(Comparator.comparing(TicketType::getPrice).thenComparing(TicketType::getName))
                .toList();

        Map<Long, List<TicketType>> ticketTypesBySession = ticketTypes.stream()
                .collect(Collectors.groupingBy(TicketType::getSessionId));

        List<Long> ticketTypeIds = ticketTypes.stream().map(TicketType::getId).toList();
        Map<Long, Inventory> inventoryByTicketType = ticketTypeIds.isEmpty() ? Map.of() : inventoryRepository
                .findByIdsIn(ticketTypeIds).stream()
                .collect(Collectors.toMap(Inventory::getTicketTypeId, Function.identity()));

        return sessions.stream()
                .map(session -> new SessionDetail(
                        session.getId(),
                        session.getStartTime(),
                        session.getEndTime(),
                        session.getSalesStartAt(),
                        session.getSalesEndAt(),
                        session.getStatus(),
                        ticketTypesBySession.getOrDefault(session.getId(), List.of()).stream()
                                .map(ticketType -> toTicketTypeDetail(ticketType, inventoryByTicketType.get(ticketType.getId())))
                                .toList()
                ))
                .toList();
    }

    private TicketTypeDetail toTicketTypeDetail(TicketType ticketType, Inventory inventory) {
        int total = inventory != null ? inventory.getTotalStock() : 0;
        int sold = inventory != null ? inventory.getSoldStock() : 0;
        int available = inventory != null
                ? inventory.getTotalStock() - inventory.getReservedStock() - inventory.getSoldStock()
                : 0;

        return new TicketTypeDetail(
                ticketType.getId(),
                ticketType.getName(),
                ticketType.getDescription(),
                ticketType.getPrice(),
                ticketType.getMaxPerUser(),
                ticketType.getStatus(),
                new InventoryInfo(available, sold, total)
        );
    }
}
