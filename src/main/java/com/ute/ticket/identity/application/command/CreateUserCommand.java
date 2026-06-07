package com.ute.ticket.identity.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserCommand {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String fullName;
    private String phone;
    private String avatarUrl;
}
