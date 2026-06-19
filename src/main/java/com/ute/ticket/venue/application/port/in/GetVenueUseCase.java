package com.ute.ticket.venue.application.port.in;

import com.ute.ticket.venue.application.result.VenueResult;

public interface GetVenueUseCase {
    VenueResult execute(Long venueId);
}
