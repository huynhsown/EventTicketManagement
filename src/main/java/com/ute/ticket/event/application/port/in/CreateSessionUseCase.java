package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.CreateSessionCommand;
import com.ute.ticket.event.application.result.SessionResult;

public interface CreateSessionUseCase {
    SessionResult execute(CreateSessionCommand cmd);
}
