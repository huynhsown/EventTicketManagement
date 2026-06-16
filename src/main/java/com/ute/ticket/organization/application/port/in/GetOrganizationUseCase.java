package com.ute.ticket.organization.application.port.in;

import com.ute.ticket.organization.application.result.OrganizationResult;

public interface GetOrganizationUseCase {
    OrganizationResult execute(String slug);
}
