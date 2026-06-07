package com.ute.ticket.identity.application.port.out;

import com.ute.ticket.identity.application.command.CreateUserCommand;

public interface IdentityProvider {
    String createUser(CreateUserCommand cmd);
    void deleteUser(String authId);
}
