package com.ute.ticket.identity.application.result;

public record LoginResult(
        String accessToken,
        String refreshToken,
        Long expiresIn,
        String tokenType
) {
}
