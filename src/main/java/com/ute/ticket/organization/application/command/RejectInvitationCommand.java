package com.ute.ticket.organization.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RejectInvitationCommand {
    private String token;
    private Long userId;
}
