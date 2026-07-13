package com.ute.ticket.event.presentation.mapper;

import com.ute.ticket.event.application.command.AssignVenueCommand;
import com.ute.ticket.event.presentation.dto.AssignVenueRequest;
import org.springframework.stereotype.Component;

@Component
public class AssignVenueMapper {

    public AssignVenueCommand toCommand(Long eventId, Long userId, AssignVenueRequest request) {
        return AssignVenueCommand.builder()
                .eventId(eventId)
                .userId(userId)
                .venueId(request.getVenueId())
                .build();
    }
}
