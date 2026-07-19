package com.ute.ticket.event.presentation.mapper;

import com.ute.ticket.event.application.result.EventDetailResult;
import com.ute.ticket.event.application.result.EventDetailResult.CategoryRef;
import com.ute.ticket.event.application.result.EventDetailResult.InventoryInfo;
import com.ute.ticket.event.application.result.EventDetailResult.OrganizationRef;
import com.ute.ticket.event.application.result.EventDetailResult.SessionDetail;
import com.ute.ticket.event.application.result.EventDetailResult.TicketTypeDetail;
import com.ute.ticket.event.application.result.EventDetailResult.VenueRef;
import com.ute.ticket.event.presentation.dto.EventDetailResponse;
import com.ute.ticket.event.presentation.dto.EventDetailResponse.Category;
import com.ute.ticket.event.presentation.dto.EventDetailResponse.Inventory;
import com.ute.ticket.event.presentation.dto.EventDetailResponse.Organization;
import com.ute.ticket.event.presentation.dto.EventDetailResponse.Session;
import com.ute.ticket.event.presentation.dto.EventDetailResponse.TicketType;
import com.ute.ticket.event.presentation.dto.EventDetailResponse.Venue;
import org.springframework.stereotype.Component;

@Component
public class EventDetailMapper {

    public EventDetailResponse toResponse(EventDetailResult result) {
        if (result == null) {
            return null;
        }
        return new EventDetailResponse(
                result.id(),
                result.title(),
                result.description(),
                result.status(),
                result.salesPaused(),
                result.bannerUrl(),
                toOrganization(result.organization()),
                toVenue(result.venue()),
                result.categories().stream().map(this::toCategory).toList(),
                result.sessions().stream().map(this::toSession).toList(),
                result.publishedAt(),
                result.createdAt(),
                result.updatedAt()
        );
    }

    private Organization toOrganization(OrganizationRef ref) {
        return ref == null ? null : new Organization(ref.id(), ref.name(), ref.logoUrl(), ref.slug());
    }

    private Venue toVenue(VenueRef ref) {
        return ref == null ? null : new Venue(ref.id(), ref.name(), ref.address(), ref.city());
    }

    private Category toCategory(CategoryRef ref) {
        return new Category(ref.id(), ref.name(), ref.slug());
    }

    private Session toSession(SessionDetail detail) {
        return new Session(
                detail.id(),
                detail.startTime(),
                detail.endTime(),
                detail.salesStartAt(),
                detail.salesEndAt(),
                detail.status(),
                detail.ticketTypes().stream().map(this::toTicketType).toList()
        );
    }

    private TicketType toTicketType(TicketTypeDetail detail) {
        return new TicketType(
                detail.id(),
                detail.name(),
                detail.description(),
                detail.price(),
                detail.maxPerUser(),
                detail.status(),
                toInventory(detail.inventory())
        );
    }

    private Inventory toInventory(InventoryInfo info) {
        return info == null ? null : new Inventory(info.available(), info.sold(), info.total());
    }
}
