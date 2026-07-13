package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.AssignVenueCommand;
import com.ute.ticket.event.application.result.EventResult;

public interface AssignVenueUseCase {
    EventResult execute(AssignVenueCommand cmd);
}
