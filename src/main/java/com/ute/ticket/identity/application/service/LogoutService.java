package com.ute.ticket.identity.application.service;

import com.ute.ticket.identity.application.command.LogoutCommand;
import com.ute.ticket.identity.application.port.in.LogoutUseCase;
import com.ute.ticket.identity.application.port.out.AuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

    private final AuthenticationProvider authenticationProvider;

    @Override
    public void logout(LogoutCommand cmd) {
        authenticationProvider.logout(cmd.getRefreshToken());
    }
}
