package com.ute.ticket.venue.application.service;

import com.ute.ticket.shared.exception.NotFoundException;
import com.ute.ticket.venue.application.command.ChangeVenueStatusCommand;
import com.ute.ticket.venue.application.port.in.ChangeVenueStatusUseCase;
import com.ute.ticket.venue.application.port.out.VenueRepository;
import com.ute.ticket.venue.application.result.VenueResult;
import com.ute.ticket.venue.domain.entity.Venue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChangeVenueStatusService implements ChangeVenueStatusUseCase {

    private final VenueRepository venueRepository;

    @Override
    public VenueResult execute(ChangeVenueStatusCommand cmd) {
        Venue venue = venueRepository.findById(cmd.getVenueId())
                .orElseThrow(() -> new NotFoundException("Venue not found"));

        venue.changeStatus(cmd.getStatus());
        venue = venueRepository.save(venue);

        return VenueResult.from(venue);
    }
}
