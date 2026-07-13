package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.PublishEventCommand;
import com.ute.ticket.event.application.result.EventResult;

public interface PublishEventUseCase {
    EventResult execute(PublishEventCommand cmd);
}
