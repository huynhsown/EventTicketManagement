package com.ute.ticket.event.application.facade;

import com.ute.ticket.event.application.command.CreateSessionCommand;
import com.ute.ticket.event.application.port.in.CreateSessionUseCase;
import com.ute.ticket.event.application.result.SessionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionFacade {

    private final CreateSessionUseCase createSessionUseCase;

    public SessionResult createSession(CreateSessionCommand cmd) {
        return createSessionUseCase.execute(cmd);
    }
}
