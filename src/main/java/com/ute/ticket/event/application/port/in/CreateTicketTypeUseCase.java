package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.CreateTicketTypeCommand;
import com.ute.ticket.event.application.result.TicketTypeResult;

public interface CreateTicketTypeUseCase {
    TicketTypeResult execute(CreateTicketTypeCommand cmd);
}
