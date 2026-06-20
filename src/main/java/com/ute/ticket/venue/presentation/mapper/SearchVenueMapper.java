package com.ute.ticket.venue.presentation.mapper;

import com.ute.ticket.venue.application.command.SearchVenueCommand;
import com.ute.ticket.venue.domain.enums.VenueStatus;
import org.springframework.stereotype.Component;

@Component
public class SearchVenueMapper {

    public SearchVenueCommand toCommand(
            String keyword,
            String city,
            VenueStatus status,
            int page,
            int size,
            String sortBy,
            boolean ascending
    ) {
        return SearchVenueCommand.builder()
                .keyword(keyword)
                .city(city)
                .status(status)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .ascending(ascending)
                .build();
    }
}
