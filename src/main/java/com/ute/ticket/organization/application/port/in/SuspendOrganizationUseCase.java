package com.ute.ticket.organization.application.port.in;

import com.ute.ticket.organization.application.command.SuspendOrganizationCommand;
import com.ute.ticket.organization.application.result.OrganizationResult;

public interface SuspendOrganizationUseCase {
    OrganizationResult execute(SuspendOrganizationCommand cmd);
}
