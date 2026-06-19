package com.ute.ticket.venue.application.port.in;

import com.ute.ticket.venue.application.command.ChangeVenueStatusCommand;
import com.ute.ticket.venue.application.result.VenueResult;

public interface ChangeVenueStatusUseCase {
    VenueResult execute(ChangeVenueStatusCommand cmd);
}
