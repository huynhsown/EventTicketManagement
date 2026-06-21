package com.ute.ticket.organization.application.port.in;

import com.ute.ticket.organization.application.command.AcceptInvitationCommand;
import com.ute.ticket.organization.application.result.OrganizationMemberResult;

public interface AcceptInvitationUseCase {
    OrganizationMemberResult execute(AcceptInvitationCommand cmd);
}
