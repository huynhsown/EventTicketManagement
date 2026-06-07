package com.ute.ticket.identity.presentation.mapper;

import com.ute.ticket.identity.application.command.CreateUserCommand;
import com.ute.ticket.identity.presentation.dto.RegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {

    public CreateUserCommand toCommand(RegisterRequest request) {
        return CreateUserCommand.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .confirmPassword(request.getConfirmPassword())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .avatarUrl(request.getAvatarUrl())
                .build();
    }
}
