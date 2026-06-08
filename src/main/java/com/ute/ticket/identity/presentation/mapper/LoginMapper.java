package com.ute.ticket.identity.presentation.mapper;

import com.ute.ticket.identity.application.command.LoginCommand;
import com.ute.ticket.identity.presentation.dto.LoginRequest;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    public LoginCommand toCommand(LoginRequest request) {
        return LoginCommand.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
}
