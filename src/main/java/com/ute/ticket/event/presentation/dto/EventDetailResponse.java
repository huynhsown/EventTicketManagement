package com.ute.ticket.event.presentation.dto;

import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Schema(description = "Public event detail returned for a given event slug")
public record EventDetailResponse(
        @Schema(description = "Event id") Long id,
        @Schema(description = "Event title") String title,
        @Schema(description = "Event description") String description,
        @Schema(description = "Canonical event lifecycle status") EventStatus status,
        @Schema(description = "True when the event's sales are paused (status == SALES_PAUSED)") boolean salesPaused,
        @Schema(description = "Event banner image URL") String bannerUrl,
        @Schema(description = "Owning organization") Organization organization,
        @Schema(description = "Venue, null when no venue is assigned") Venue venue,
        @Schema(description = "Categories the event belongs to") List<Category> categories,
        @Schema(description = "On-sale sessions") List<Session> sessions,
        @Schema(description = "Publication timestamp (ISO-8601 UTC)") Instant publishedAt,
        @Schema(description = "Creation timestamp (ISO-8601 UTC)") Instant createdAt,
        @Schema(description = "Last-update timestamp (ISO-8601 UTC)") Instant updatedAt
) {

    @Schema(description = "Owning organization")
    public record Organization(
            @Schema(description = "Organization id") Long id,
            @Schema(description = "Organization name") String name,
            @Schema(description = "Organization logo URL") String logoUrl,
            @Schema(description = "Organization slug") String slug
    ) {
    }

    @Schema(description = "Venue")
    public record Venue(
            @Schema(description = "Venue id") Long id,
            @Schema(description = "Venue name") String name,
            @Schema(description = "Venue address") String address,
            @Schema(description = "Venue city") String city
    ) {
    }

    @Schema(description = "Category the event belongs to")
    public record Category(
            @Schema(description = "Category id") Long id,
            @Schema(description = "Category name") String name,
            @Schema(description = "Category slug") String slug
    ) {
    }

    @Schema(description = "On-sale session with its active ticket types")
    public record Session(
            @Schema(description = "Session id") Long id,
            @Schema(description = "Session start time (ISO-8601 UTC)") Instant startTime,
            @Schema(description = "Session end time (ISO-8601 UTC)") Instant endTime,
            @Schema(description = "Sales window start (ISO-8601 UTC)") Instant salesStartAt,
            @Schema(description = "Sales window end (ISO-8601 UTC)") Instant salesEndAt,
            @Schema(description = "Canonical session lifecycle status") SessionStatus status,
            @Schema(description = "Active ticket types of this session") List<TicketType> ticketTypes
    ) {
    }

    @Schema(description = "Active ticket type with its availability")
    public record TicketType(
            @Schema(description = "Ticket type id") Long id,
            @Schema(description = "Ticket type name") String name,
            @Schema(description = "Ticket type description") String description,
            @Schema(description = "Ticket price") BigDecimal price,
            @Schema(description = "Maximum tickets purchasable per user") Integer maxPerUser,
            @Schema(description = "Canonical ticket type status") TicketTypeStatus status,
            @Schema(description = "Per-ticket-type inventory") Inventory inventory
    ) {
    }

    @Schema(description = "Inventory availability summary")
    public record Inventory(
            @Schema(description = "Tickets currently available to buy") int available,
            @Schema(description = "Tickets sold") int sold,
            @Schema(description = "Total tickets in stock") int total
    ) {
    }
}
