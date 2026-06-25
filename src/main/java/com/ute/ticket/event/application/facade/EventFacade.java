package com.ute.ticket.event.application.facade;

import com.ute.ticket.event.application.command.CreateEventCommand;
import com.ute.ticket.event.application.port.in.CreateEventUseCase;
import com.ute.ticket.event.application.result.EventResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventFacade {

    private final CreateEventUseCase createEventUseCase;

    public EventResult createEvent(CreateEventCommand cmd) {
        return createEventUseCase.execute(cmd);
    }
}
