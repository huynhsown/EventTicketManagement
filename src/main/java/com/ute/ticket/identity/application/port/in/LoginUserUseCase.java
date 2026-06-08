package com.ute.ticket.identity.application.port.in;

import com.ute.ticket.identity.application.command.LoginCommand;
import com.ute.ticket.identity.application.result.LoginResult;

public interface LoginUserUseCase {
    LoginResult login(LoginCommand cmd);
}
