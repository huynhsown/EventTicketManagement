package com.ute.ticket.venue.presentation.mapper;

import com.ute.ticket.venue.application.command.ChangeVenueStatusCommand;
import com.ute.ticket.venue.presentation.dto.ChangeVenueStatusRequest;
import org.springframework.stereotype.Component;

@Component
public class ChangeVenueStatusMapper {

    public ChangeVenueStatusCommand toCommand(Long venueId, ChangeVenueStatusRequest request) {
        return ChangeVenueStatusCommand.builder()
                .venueId(venueId)
                .status(request.getStatus())
                .build();
    }
}
