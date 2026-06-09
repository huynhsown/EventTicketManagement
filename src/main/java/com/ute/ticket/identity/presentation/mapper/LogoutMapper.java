package com.ute.ticket.identity.presentation.mapper;

import com.ute.ticket.identity.application.command.LogoutCommand;
import com.ute.ticket.identity.presentation.dto.LogoutRequest;
import org.springframework.stereotype.Component;

@Component
public class LogoutMapper {

    public LogoutCommand toCommand(LogoutRequest request) {
        return LogoutCommand.builder()
                .refreshToken(request.getRefreshToken())
                .build();
    }
}
