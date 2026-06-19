package com.ute.ticket.venue.application.service;

import com.ute.ticket.shared.exception.NotFoundException;
import com.ute.ticket.venue.application.command.UpdateVenueCommand;
import com.ute.ticket.venue.application.port.in.UpdateVenueUseCase;
import com.ute.ticket.venue.application.port.out.VenueRepository;
import com.ute.ticket.venue.application.result.VenueResult;
import com.ute.ticket.venue.domain.entity.Venue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateVenueService implements UpdateVenueUseCase {

    private final VenueRepository venueRepository;

    @Override
    public VenueResult execute(UpdateVenueCommand cmd) {
        Venue venue = venueRepository.findById(cmd.getVenueId())
                .orElseThrow(() -> new NotFoundException("Venue not found"));

        venue.update(
                cmd.getName(),
                cmd.getAddress(),
                cmd.getCity(),
                cmd.getCountry(),
                cmd.getLatitude(),
                cmd.getLongitude(),
                cmd.getCapacity(),
                cmd.getDescription()
        );

        venue = venueRepository.save(venue);

        return VenueResult.from(venue);
    }
}
