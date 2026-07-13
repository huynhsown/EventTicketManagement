package com.ute.ticket.event.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssignVenueCommand {

    private Long eventId;
    private Long userId;
    private Long venueId;
}
