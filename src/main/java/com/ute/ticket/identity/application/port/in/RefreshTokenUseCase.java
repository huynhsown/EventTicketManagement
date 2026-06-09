package com.ute.ticket.identity.application.port.in;

import com.ute.ticket.identity.application.command.RefreshTokenCommand;
import com.ute.ticket.identity.application.result.LoginResult;

public interface RefreshTokenUseCase {
    LoginResult refreshToken(RefreshTokenCommand cmd);
}
