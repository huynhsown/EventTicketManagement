package com.ute.ticket.identity.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginCommand {

    private String email;
    private String password;
}
