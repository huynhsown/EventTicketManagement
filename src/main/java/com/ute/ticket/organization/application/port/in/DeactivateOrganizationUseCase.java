package com.ute.ticket.organization.application.port.in;

import com.ute.ticket.organization.application.command.DeactivateOrganizationCommand;
import com.ute.ticket.organization.application.result.OrganizationResult;

public interface DeactivateOrganizationUseCase {
    OrganizationResult execute(DeactivateOrganizationCommand cmd);
}
