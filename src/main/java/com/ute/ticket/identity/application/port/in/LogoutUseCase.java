package com.ute.ticket.identity.application.port.in;

import com.ute.ticket.identity.application.command.LogoutCommand;

public interface LogoutUseCase {
    void logout(LogoutCommand cmd);
}
