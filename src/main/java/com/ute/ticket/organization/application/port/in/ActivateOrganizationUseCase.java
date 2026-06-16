package com.ute.ticket.organization.application.port.in;

import com.ute.ticket.organization.application.command.ActivateOrganizationCommand;
import com.ute.ticket.organization.application.result.OrganizationResult;

public interface ActivateOrganizationUseCase {
    OrganizationResult execute(ActivateOrganizationCommand cmd);
}
