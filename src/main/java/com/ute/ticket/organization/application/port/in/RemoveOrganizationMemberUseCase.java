package com.ute.ticket.organization.application.port.in;

import com.ute.ticket.organization.application.command.RemoveOrganizationMemberCommand;
import com.ute.ticket.organization.application.result.OrganizationMemberResult;

public interface RemoveOrganizationMemberUseCase {
    OrganizationMemberResult execute(RemoveOrganizationMemberCommand cmd);
}
