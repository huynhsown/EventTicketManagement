package com.ute.ticket.search.presentation.mapper;

import com.ute.ticket.search.application.command.SearchEventCommand;
import com.ute.ticket.shared.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class SearchEventMapper {

    private static final int MAX_PAGE_SIZE = 100;
    private static final String DEFAULT_SORT_BY = "id";
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id",
            "title",
            "organizationName",
            "venueCity",
            "minPrice",
            "maxPrice",
            "publishedAt",
            "createdAt",
            "updatedAt"
    );

    public SearchEventCommand toCommand(
            String keyword,
            String city,
            List<String> categorySlugs,
            String status,
            Double minPrice,
            Double maxPrice,
            Boolean hasAvailableTickets,
            int page,
            int size,
            String sortBy,
            boolean ascending
    ) {
        validatePage(page);
        validateSize(size);
        validatePriceRange(minPrice, maxPrice);
        String normalizedSortBy = normalizeSortBy(sortBy);

        return SearchEventCommand.builder()
                .keyword(keyword)
                .city(city)
                .categorySlugs(categorySlugs)
                .status(status)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .hasAvailableTickets(hasAvailableTickets)
                .page(page)
                .size(size)
                .sortBy(normalizedSortBy)
                .ascending(ascending)
                .build();
    }

    private void validatePage(int page) {
        if (page < 0) {
            throw new BadRequestException("Page cannot be less than zero");
        }
    }

    private void validateSize(int size) {
        if (size <= 0) {
            throw new BadRequestException("Size must be greater than zero");
        }

        if (size > MAX_PAGE_SIZE) {
            throw new BadRequestException("Size cannot be more than 100");
        }
    }

    private void validatePriceRange(Double minPrice, Double maxPrice) {
        if (minPrice != null && minPrice < 0) {
            throw new BadRequestException("minPrice cannot be negative");
        }

        if (maxPrice != null && maxPrice < 0) {
            throw new BadRequestException("maxPrice cannot be negative");
        }

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new BadRequestException("minPrice cannot be greater than maxPrice");
        }
    }

    private String normalizeSortBy(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return DEFAULT_SORT_BY;
        }

        String normalized = sortBy.trim();
        if (!ALLOWED_SORT_FIELDS.contains(normalized)) {
            throw new BadRequestException("Unsupported event sort field: " + normalized);
        }

        return normalized;
    }
}
