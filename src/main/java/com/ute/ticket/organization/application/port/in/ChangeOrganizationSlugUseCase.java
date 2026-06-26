package com.ute.ticket.organization.application.port.in;

import com.ute.ticket.organization.application.command.ChangeOrganizationSlugCommand;
import com.ute.ticket.organization.application.result.OrganizationResult;

public interface ChangeOrganizationSlugUseCase {
    OrganizationResult execute(ChangeOrganizationSlugCommand cmd);
}
