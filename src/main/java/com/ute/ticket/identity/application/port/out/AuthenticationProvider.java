package com.ute.ticket.identity.application.port.out;

import com.ute.ticket.identity.application.result.LoginResult;

public interface AuthenticationProvider {
    LoginResult authenticate(String email, String password);
    LoginResult refreshToken(String refreshToken);
    void logout(String refreshToken);
}
