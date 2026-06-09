package com.ute.ticket.identity.application.service;

import com.ute.ticket.identity.application.command.RefreshTokenCommand;
import com.ute.ticket.identity.application.port.in.RefreshTokenUseCase;
import com.ute.ticket.identity.application.port.out.AuthenticationProvider;
import com.ute.ticket.identity.application.result.LoginResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

    private final AuthenticationProvider authenticationProvider;

    @Override
    public LoginResult refreshToken(RefreshTokenCommand cmd) {
        return authenticationProvider.refreshToken(cmd.getRefreshToken());
    }
}
