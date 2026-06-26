package com.ute.ticket.organization.application.port.in;

import com.ute.ticket.organization.application.command.ChangeMemberRoleCommand;
import com.ute.ticket.organization.application.result.OrganizationMemberResult;

public interface ChangeMemberRoleUseCase {
    OrganizationMemberResult execute(ChangeMemberRoleCommand cmd);
}
