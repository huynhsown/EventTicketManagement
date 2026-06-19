package com.ute.ticket.venue.application.service;

import com.ute.ticket.venue.application.command.CreateVenueCommand;
import com.ute.ticket.venue.application.port.in.CreateVenueUseCase;
import com.ute.ticket.venue.application.port.out.VenueRepository;
import com.ute.ticket.venue.application.result.VenueResult;
import com.ute.ticket.venue.domain.entity.Venue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateVenueService implements CreateVenueUseCase {

    private final VenueRepository venueRepository;

    @Override
    public VenueResult execute(CreateVenueCommand cmd) {
        Venue venue = Venue.create(
                cmd.getName(),
                cmd.getAddress(),
                cmd.getCity(),
                cmd.getCountry(),
                cmd.getLatitude(),
                cmd.getLongitude(),
                cmd.getCapacity(),
                cmd.getDescription(),
                cmd.getStatus()
        );

        venue = venueRepository.save(venue);

        return VenueResult.from(venue);
    }
}
