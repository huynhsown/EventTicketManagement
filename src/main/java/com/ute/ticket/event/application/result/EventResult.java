package com.ute.ticket.event.application.result;

import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.enums.EventStatus;

public record EventResult(
        Long id,
        Long organizationId,
        Long venueId,
        String title,
        String slug,
        String description,
        String bannerUrl,
        EventStatus status
) {
    public static EventResult from(Event event) {
        return new EventResult(
                event.getId(),
                event.getOrganizationId(),
                event.getVenueId(),
                event.getTitle(),
                event.getSlug(),
                event.getDescription(),
                event.getBannerUrl(),
                event.getStatus()
        );
    }
}
