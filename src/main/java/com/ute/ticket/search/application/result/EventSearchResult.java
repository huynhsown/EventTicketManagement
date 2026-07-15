package com.ute.ticket.search.application.result;

import java.time.Instant;
import java.util.List;

public record EventSearchResult(
        Long id,
        String title,
        String description,
        String status,
        String bannerUrl,
        Long organizationId,
        String organizationName,
        Long venueId,
        String venueName,
        String venueCity,
        List<Long> categoryIds,
        List<String> categoryNames,
        List<String> categorySlugs,
        Double minPrice,
        Double maxPrice,
        Boolean hasAvailableTickets,
        Instant publishedAt,
        Instant createdAt,
        Instant updatedAt
) {
}
