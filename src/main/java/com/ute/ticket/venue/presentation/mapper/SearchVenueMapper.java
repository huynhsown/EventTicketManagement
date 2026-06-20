package com.ute.ticket.venue.presentation.mapper;

import com.ute.ticket.shared.exception.BadRequestException;
import com.ute.ticket.venue.application.command.SearchVenueCommand;
import com.ute.ticket.venue.domain.enums.VenueStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SearchVenueMapper {

    private static final int MAX_PAGE_SIZE = 100;
    private static final String DEFAULT_SORT_BY = "id";
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id",
            "name",
            "city",
            "country",
            "capacity",
            "status",
            "createdAt"
    );

    public SearchVenueCommand toCommand(
            String keyword,
            String city,
            VenueStatus status,
            int page,
            int size,
            String sortBy,
            boolean ascending
    ) {
        validatePage(page);
        validateSize(size);
        String normalizedSortBy = normalizeSortBy(sortBy);

        return SearchVenueCommand.builder()
                .keyword(keyword)
                .city(city)
                .status(status)
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

    private String normalizeSortBy(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return DEFAULT_SORT_BY;
        }

        String normalized = sortBy.trim();
        if (!ALLOWED_SORT_FIELDS.contains(normalized)) {
            throw new BadRequestException("Unsupported venue sort field: " + normalized);
        }

        return normalized;
    }
}
