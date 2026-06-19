package com.ute.ticket.venue.application.command;

import com.ute.ticket.venue.domain.enums.VenueStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangeVenueStatusCommand {

    private Long venueId;
    private VenueStatus status;
}
