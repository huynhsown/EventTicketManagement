package com.ute.ticket.event.application.result;

import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record EventDetailResult(
        Long id,
        String title,
        String description,
        EventStatus status,
        boolean salesPaused,
        String bannerUrl,
        OrganizationRef organization,
        VenueRef venue,
        List<CategoryRef> categories,
        List<SessionDetail> sessions,
        Instant publishedAt,
        Instant createdAt,
        Instant updatedAt
) {

    public record OrganizationRef(
            Long id,
            String name,
            String logoUrl,
            String slug
    ) {
    }

    public record VenueRef(
            Long id,
            String name,
            String address,
            String city
    ) {
    }

    public record CategoryRef(
            Long id,
            String name,
            String slug
    ) {
    }

    public record SessionDetail(
            Long id,
            Instant startTime,
            Instant endTime,
            Instant salesStartAt,
            Instant salesEndAt,
            SessionStatus status,
            List<TicketTypeDetail> ticketTypes
    ) {
    }

    public record TicketTypeDetail(
            Long id,
            String name,
            String description,
            BigDecimal price,
            Integer maxPerUser,
            TicketTypeStatus status,
            InventoryInfo inventory
    ) {
    }

    public record InventoryInfo(
            int available,
            int sold,
            int total
    ) {
    }
}
