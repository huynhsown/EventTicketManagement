package com.ute.ticket.venue.application.port.in;

import com.ute.ticket.venue.application.command.CreateVenueCommand;
import com.ute.ticket.venue.application.result.VenueResult;

public interface CreateVenueUseCase {
    VenueResult execute(CreateVenueCommand cmd);
}
