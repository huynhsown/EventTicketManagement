package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.PublishSessionsCommand;
import com.ute.ticket.event.application.result.SessionResult;

import java.util.List;

public interface PublishSessionsUseCase {
    List<SessionResult> execute(PublishSessionsCommand cmd);
}