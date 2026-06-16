package com.ute.ticket.organization.presentation.mapper;

import com.ute.ticket.organization.application.command.CreateOrganizationCommand;
import com.ute.ticket.organization.presentation.dto.CreateOrganizationRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateOrganizationMapper {

    public CreateOrganizationCommand toCommand(CreateOrganizationRequest request, Long ownerId) {
        return CreateOrganizationCommand.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .logoUrl(request.getLogoUrl())
                .website(request.getWebsite())
                .ownerId(ownerId)
                .build();
    }
}
