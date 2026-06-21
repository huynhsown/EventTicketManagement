package com.ute.ticket.venue.application.service;

import com.ute.ticket.shared.exception.NotFoundException;
import com.ute.ticket.venue.application.port.in.DeleteVenueUseCase;
import com.ute.ticket.venue.application.port.out.VenueRepository;
import com.ute.ticket.venue.domain.entity.Venue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteVenueService implements DeleteVenueUseCase {

    private final VenueRepository venueRepository;

    @Override
    public void execute(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new NotFoundException("Venue not found"));

        venue.delete();
        venueRepository.save(venue);
    }
}
