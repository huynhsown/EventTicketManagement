package com.ute.ticket.organization.application.port.in;

import com.ute.ticket.organization.application.command.RejectInvitationCommand;
import com.ute.ticket.organization.application.result.OrganizationMemberResult;

public interface RejectInvitationUseCase {
    OrganizationMemberResult execute(RejectInvitationCommand cmd);
}
