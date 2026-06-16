package com.ute.ticket.organization.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeactivateOrganizationCommand {
    private Long organizationId;
    private Long userId;
}
