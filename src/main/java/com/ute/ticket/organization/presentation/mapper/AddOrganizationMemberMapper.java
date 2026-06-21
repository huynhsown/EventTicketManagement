package com.ute.ticket.organization.presentation.mapper;

import com.ute.ticket.organization.application.command.AddOrganizationMemberCommand;
import com.ute.ticket.organization.presentation.dto.AddOrganizationMemberRequest;
import org.springframework.stereotype.Component;

@Component
public class AddOrganizationMemberMapper {

    public AddOrganizationMemberCommand toCommand(Long organizationId, AddOrganizationMemberRequest request, Long addedBy) {
        return AddOrganizationMemberCommand.builder()
                .organizationId(organizationId)
                .userId(request.getUserId())
                .role(request.getRole())
                .addedBy(addedBy)
                .build();
    }
}
