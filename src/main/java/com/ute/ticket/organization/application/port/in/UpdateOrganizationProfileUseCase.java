package com.ute.ticket.organization.application.port.in;

import com.ute.ticket.organization.application.command.UpdateOrganizationProfileCommand;
import com.ute.ticket.organization.application.result.OrganizationResult;

public interface UpdateOrganizationProfileUseCase {
    OrganizationResult execute(UpdateOrganizationProfileCommand cmd);
}
