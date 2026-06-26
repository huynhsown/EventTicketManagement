package com.ute.ticket.organization.application.port.in;

import com.ute.ticket.organization.application.command.LeaveOrganizationCommand;
import com.ute.ticket.organization.application.result.OrganizationMemberResult;

public interface LeaveOrganizationUseCase {
    OrganizationMemberResult execute(LeaveOrganizationCommand cmd);
}
