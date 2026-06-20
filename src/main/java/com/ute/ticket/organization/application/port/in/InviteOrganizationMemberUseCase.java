package com.ute.ticket.organization.application.port.in;

import com.ute.ticket.organization.application.command.AddOrganizationMemberCommand;
import com.ute.ticket.organization.application.result.OrganizationMemberResult;

public interface InviteOrganizationMemberUseCase {
    OrganizationMemberResult execute(AddOrganizationMemberCommand cmd);
}
