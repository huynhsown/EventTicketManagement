package com.ute.ticket.event.application.facade;

import com.ute.ticket.event.application.command.CreateSessionCommand;
import com.ute.ticket.event.application.command.PublishSessionsCommand;
import com.ute.ticket.event.application.port.in.CreateSessionUseCase;
import com.ute.ticket.event.application.port.in.PublishSessionsUseCase;
import com.ute.ticket.event.application.result.SessionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SessionFacade {

    private final CreateSessionUseCase createSessionUseCase;
    private final PublishSessionsUseCase publishSessionsUseCase;

    public SessionResult createSession(CreateSessionCommand cmd) {
        return createSessionUseCase.execute(cmd);
    }

    public List<SessionResult> publishSessions(PublishSessionsCommand cmd) {
        return publishSessionsUseCase.execute(cmd);
    }
}
