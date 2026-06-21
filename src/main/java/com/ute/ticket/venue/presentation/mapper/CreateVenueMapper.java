package com.ute.ticket.venue.presentation.mapper;

import com.ute.ticket.venue.application.command.CreateVenueCommand;
import com.ute.ticket.venue.presentation.dto.CreateVenueRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateVenueMapper {

    public CreateVenueCommand toCommand(CreateVenueRequest request) {
        return CreateVenueCommand.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .capacity(request.getCapacity())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();
    }
}
