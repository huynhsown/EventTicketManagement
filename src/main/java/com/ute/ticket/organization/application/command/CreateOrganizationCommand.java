package com.ute.ticket.organization.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateOrganizationCommand {

    private String name;
    private String slug;
    private String description;
    private String logoUrl;
    private String website;
    private Long ownerId;
}
