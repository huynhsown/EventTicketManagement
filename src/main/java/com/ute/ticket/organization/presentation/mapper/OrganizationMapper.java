package com.ute.ticket.organization.presentation.mapper;

import com.ute.ticket.organization.application.command.AddOrganizationMemberCommand;
import com.ute.ticket.organization.application.command.ChangeMemberRoleCommand;
import com.ute.ticket.organization.application.command.ChangeOrganizationSlugCommand;
import com.ute.ticket.organization.application.command.CreateOrganizationCommand;
import com.ute.ticket.organization.application.command.TransferOwnershipCommand;
import com.ute.ticket.organization.application.command.UpdateOrganizationProfileCommand;
import com.ute.ticket.organization.presentation.dto.AddOrganizationMemberRequest;
import com.ute.ticket.organization.presentation.dto.ChangeMemberRoleRequest;
import com.ute.ticket.organization.presentation.dto.ChangeOrganizationSlugRequest;
import com.ute.ticket.organization.presentation.dto.CreateOrganizationRequest;
import com.ute.ticket.organization.presentation.dto.TransferOwnershipRequest;
import com.ute.ticket.organization.presentation.dto.UpdateOrganizationProfileRequest;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {

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

    public AddOrganizationMemberCommand toCommand(Long organizationId, AddOrganizationMemberRequest request, Long addedBy) {
        return AddOrganizationMemberCommand.builder()
                .organizationId(organizationId)
                .userId(request.getUserId())
                .role(request.getRole())
                .addedBy(addedBy)
                .build();
    }

    public UpdateOrganizationProfileCommand toCommand(
            Long organizationId,
            UpdateOrganizationProfileRequest request,
            Long userId
    ) {
        return UpdateOrganizationProfileCommand.builder()
                .organizationId(organizationId)
                .userId(userId)
                .name(request.getName())
                .description(request.getDescription())
                .logoUrl(request.getLogoUrl())
                .website(request.getWebsite())
                .build();
    }

    public ChangeOrganizationSlugCommand toCommand(
            Long organizationId,
            ChangeOrganizationSlugRequest request,
            Long userId
    ) {
        return ChangeOrganizationSlugCommand.builder()
                .organizationId(organizationId)
                .userId(userId)
                .newSlug(request.getNewSlug())
                .build();
    }

    public TransferOwnershipCommand toCommand(
            Long organizationId,
            TransferOwnershipRequest request,
            Long userId
    ) {
        return TransferOwnershipCommand.builder()
                .organizationId(organizationId)
                .userId(userId)
                .targetUserId(request.getTargetUserId())
                .build();
    }

    public ChangeMemberRoleCommand toCommand(
            Long organizationId,
            Long targetUserId,
            ChangeMemberRoleRequest request,
            Long userId
    ) {
        return ChangeMemberRoleCommand.builder()
                .organizationId(organizationId)
                .userId(userId)
                .targetUserId(targetUserId)
                .role(request.getRole())
                .build();
    }
}
