package com.ute.ticket.identity.application.service;

import com.ute.ticket.identity.application.command.LoginCommand;
import com.ute.ticket.identity.application.port.in.LoginUserUseCase;
import com.ute.ticket.identity.application.port.out.AuthenticationProvider;
import com.ute.ticket.identity.application.result.LoginResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserService implements LoginUserUseCase {

    private final AuthenticationProvider authenticationProvider;

    @Override
    public LoginResult login(LoginCommand cmd) {
        return authenticationProvider.authenticate(cmd.getEmail(), cmd.getPassword());
    }
}
