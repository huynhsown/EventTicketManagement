package com.ute.ticket.venue.presentation.mapper;

import com.ute.ticket.venue.application.command.UpdateVenueCommand;
import com.ute.ticket.venue.presentation.dto.UpdateVenueRequest;
import org.springframework.stereotype.Component;

@Component
public class UpdateVenueMapper {

    public UpdateVenueCommand toCommand(Long venueId, UpdateVenueRequest request) {
        return UpdateVenueCommand.builder()
                .venueId(venueId)
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .capacity(request.getCapacity())
                .description(request.getDescription())
                .build();
    }
}
