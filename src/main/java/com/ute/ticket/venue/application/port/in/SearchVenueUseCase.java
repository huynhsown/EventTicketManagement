package com.ute.ticket.venue.application.port.in;

import com.ute.ticket.shared.dto.PageInfo;
import com.ute.ticket.venue.application.command.SearchVenueCommand;
import com.ute.ticket.venue.application.result.VenueResult;

public interface SearchVenueUseCase {
    PageInfo<VenueResult> execute(SearchVenueCommand cmd);
}
