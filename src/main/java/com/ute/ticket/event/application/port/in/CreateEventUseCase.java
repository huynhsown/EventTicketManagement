package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.CreateEventCommand;
import com.ute.ticket.event.application.result.EventResult;

public interface CreateEventUseCase {
    EventResult execute(CreateEventCommand cmd);
}
