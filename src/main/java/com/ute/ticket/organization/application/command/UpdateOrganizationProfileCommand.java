package com.ute.ticket.organization.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateOrganizationProfileCommand {

    private Long organizationId;
    private Long userId;
    private String name;
    private String description;
    private String logoUrl;
    private String website;
}
