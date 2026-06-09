package com.ute.ticket.identity.presentation.mapper;

import com.ute.ticket.identity.application.command.RefreshTokenCommand;
import com.ute.ticket.identity.presentation.dto.RefreshTokenRequest;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenMapper {

    public RefreshTokenCommand toCommand(RefreshTokenRequest request) {
        return RefreshTokenCommand.builder()
                .refreshToken(request.getRefreshToken())
                .build();
    }
}
