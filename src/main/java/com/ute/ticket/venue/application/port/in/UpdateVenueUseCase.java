package com.ute.ticket.venue.application.port.in;

import com.ute.ticket.venue.application.command.UpdateVenueCommand;
import com.ute.ticket.venue.application.result.VenueResult;

public interface UpdateVenueUseCase {
    VenueResult execute(UpdateVenueCommand cmd);
}
