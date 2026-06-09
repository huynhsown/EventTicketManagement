package com.ute.ticket.identity.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenCommand {
    private String refreshToken;
}
