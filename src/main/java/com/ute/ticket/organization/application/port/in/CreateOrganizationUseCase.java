package com.ute.ticket.organization.application.port.in;

import com.ute.ticket.organization.application.command.CreateOrganizationCommand;
import com.ute.ticket.organization.application.result.OrganizationResult;

public interface CreateOrganizationUseCase {
    OrganizationResult execute(CreateOrganizationCommand cmd);
}
