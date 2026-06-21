package com.ute.ticket.venue.application.service;

import com.ute.ticket.shared.exception.NotFoundException;
import com.ute.ticket.venue.application.port.in.GetVenueUseCase;
import com.ute.ticket.venue.application.port.out.VenueRepository;
import com.ute.ticket.venue.application.result.VenueResult;
import com.ute.ticket.venue.domain.entity.Venue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetVenueService implements GetVenueUseCase {

    private final VenueRepository venueRepository;

    @Override
    public VenueResult execute(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new NotFoundException("Venue not found"));

        return VenueResult.from(venue);
    }
}
