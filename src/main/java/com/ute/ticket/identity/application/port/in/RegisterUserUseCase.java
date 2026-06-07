package com.ute.ticket.identity.application.port.in;

import com.ute.ticket.identity.application.command.CreateUserCommand;
import com.ute.ticket.identity.application.result.UserResult;

public interface RegisterUserUseCase {
    UserResult register(CreateUserCommand cmd);
}
