package com.ute.ticket.venue.application.service;

import com.ute.ticket.shared.dto.PageInfo;
import com.ute.ticket.venue.application.command.SearchVenueCommand;
import com.ute.ticket.venue.application.port.in.SearchVenueUseCase;
import com.ute.ticket.venue.application.port.out.VenueRepository;
import com.ute.ticket.venue.application.result.VenueResult;
import com.ute.ticket.venue.domain.entity.Venue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchVenueService implements SearchVenueUseCase {

    private final VenueRepository venueRepository;

    @Override
    public PageInfo<VenueResult> execute(SearchVenueCommand cmd) {
        PageInfo<Venue> venues = venueRepository.search(
                cmd.getKeyword(),
                cmd.getCity(),
                cmd.getStatus(),
                cmd.getPage(),
                cmd.getSize(),
                cmd.getSortBy(),
                cmd.isAscending()
        );

        return PageInfo.<VenueResult>builder()
                .pageContent(venues.getPageContent().stream().map(VenueResult::from).toList())
                .number(venues.getNumber())
                .size(venues.getSize())
                .totalElements(venues.getTotalElements())
                .totalPages(venues.getTotalPages())
                .empty(venues.isEmpty())
                .numberOfElements(venues.getNumberOfElements())
                .hasNextPage(venues.isHasNextPage())
                .hasPreviousPage(venues.isHasPreviousPage())
                .build();
    }
}
