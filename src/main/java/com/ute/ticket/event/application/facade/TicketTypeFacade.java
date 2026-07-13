package com.ute.ticket.event.application.facade;

import com.ute.ticket.event.application.command.CreateTicketTypeCommand;
import com.ute.ticket.event.application.port.in.CreateTicketTypeUseCase;
import com.ute.ticket.event.application.result.TicketTypeResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketTypeFacade {

    private final CreateTicketTypeUseCase createTicketTypeUseCase;

    public TicketTypeResult createTicketType(CreateTicketTypeCommand cmd) {
        return createTicketTypeUseCase.execute(cmd);
    }
}
