package com.ute.ticket.organization.application.facade;

import com.ute.ticket.organization.application.command.AcceptInvitationCommand;
import com.ute.ticket.organization.application.command.ActivateOrganizationCommand;
import com.ute.ticket.organization.application.command.AddOrganizationMemberCommand;
import com.ute.ticket.organization.application.command.ChangeMemberRoleCommand;
import com.ute.ticket.organization.application.command.ChangeOrganizationSlugCommand;
import com.ute.ticket.organization.application.command.CreateOrganizationCommand;
import com.ute.ticket.organization.application.command.DeactivateOrganizationCommand;
import com.ute.ticket.organization.application.command.LeaveOrganizationCommand;
import com.ute.ticket.organization.application.command.RejectInvitationCommand;
import com.ute.ticket.organization.application.command.RemoveOrganizationMemberCommand;
import com.ute.ticket.organization.application.command.SuspendOrganizationCommand;
import com.ute.ticket.organization.application.command.TransferOwnershipCommand;
import com.ute.ticket.organization.application.command.UpdateOrganizationProfileCommand;
import com.ute.ticket.organization.application.port.in.AcceptInvitationUseCase;
import com.ute.ticket.organization.application.port.in.ActivateOrganizationUseCase;
import com.ute.ticket.organization.application.port.in.ChangeMemberRoleUseCase;
import com.ute.ticket.organization.application.port.in.ChangeOrganizationSlugUseCase;
import com.ute.ticket.organization.application.port.in.InviteOrganizationMemberUseCase;
import com.ute.ticket.organization.application.port.in.CreateOrganizationUseCase;
import com.ute.ticket.organization.application.port.in.DeactivateOrganizationUseCase;
import com.ute.ticket.organization.application.port.in.GetOrganizationUseCase;
import com.ute.ticket.organization.application.port.in.LeaveOrganizationUseCase;
import com.ute.ticket.organization.application.port.in.RejectInvitationUseCase;
import com.ute.ticket.organization.application.port.in.RemoveOrganizationMemberUseCase;
import com.ute.ticket.organization.application.port.in.SuspendOrganizationUseCase;
import com.ute.ticket.organization.application.port.in.TransferOwnershipUseCase;
import com.ute.ticket.organization.application.port.in.UpdateOrganizationProfileUseCase;
import com.ute.ticket.organization.application.result.OrganizationMemberResult;
import com.ute.ticket.organization.application.result.OrganizationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizationFacade {

    private final CreateOrganizationUseCase createOrganizationUseCase;
    private final GetOrganizationUseCase getOrganizationUseCase;
    private final ActivateOrganizationUseCase activateOrganizationUseCase;
    private final SuspendOrganizationUseCase suspendOrganizationUseCase;
    private final DeactivateOrganizationUseCase deactivateOrganizationUseCase;
    private final UpdateOrganizationProfileUseCase updateOrganizationProfileUseCase;
    private final ChangeOrganizationSlugUseCase changeOrganizationSlugUseCase;
    private final ChangeMemberRoleUseCase changeMemberRoleUseCase;
    private final RemoveOrganizationMemberUseCase removeOrganizationMemberUseCase;
    private final LeaveOrganizationUseCase leaveOrganizationUseCase;
    private final TransferOwnershipUseCase transferOwnershipUseCase;
    private final InviteOrganizationMemberUseCase addOrganizationMemberUseCase;
    private final AcceptInvitationUseCase acceptInvitationUseCase;
    private final RejectInvitationUseCase rejectInvitationUseCase;

    public OrganizationResult createOrganization(CreateOrganizationCommand cmd) {
        return createOrganizationUseCase.execute(cmd);
    }

    public OrganizationResult getOrganization(String slug) {
        return getOrganizationUseCase.execute(slug);
    }

    public OrganizationResult activateOrganization(Long organizationId) {
        var command = ActivateOrganizationCommand.builder()
                .organizationId(organizationId)
                .build();
        return activateOrganizationUseCase.execute(command);
    }

    public OrganizationResult suspendOrganization(Long organizationId) {
        var command = SuspendOrganizationCommand.builder()
                .organizationId(organizationId)
                .build();
        return suspendOrganizationUseCase.execute(command);
    }

    public OrganizationResult deactivateOrganization(Long organizationId, Long userId) {
        var command = DeactivateOrganizationCommand.builder()
                .organizationId(organizationId)
                .userId(userId)
                .build();
        return deactivateOrganizationUseCase.execute(command);
    }

    public OrganizationResult updateOrganizationProfile(UpdateOrganizationProfileCommand command) {
        return updateOrganizationProfileUseCase.execute(command);
    }

    public OrganizationResult changeOrganizationSlug(ChangeOrganizationSlugCommand command) {
        return changeOrganizationSlugUseCase.execute(command);
    }

    public OrganizationMemberResult removeOrganizationMember(Long organizationId, Long userId, Long removedBy) {
        var command = RemoveOrganizationMemberCommand.builder()
                .organizationId(organizationId)
                .userId(userId)
                .removedBy(removedBy)
                .build();
        return removeOrganizationMemberUseCase.execute(command);
    }

    public OrganizationMemberResult leaveOrganization(Long organizationId, Long userId) {
        var command = LeaveOrganizationCommand.builder()
                .organizationId(organizationId)
                .userId(userId)
                .build();
        return leaveOrganizationUseCase.execute(command);
    }

    public OrganizationResult transferOwnership(TransferOwnershipCommand command) {
        return transferOwnershipUseCase.execute(command);
    }

    public OrganizationMemberResult changeMemberRole(ChangeMemberRoleCommand command) {
        return changeMemberRoleUseCase.execute(command);
    }

    public OrganizationMemberResult addOrganizationMember(AddOrganizationMemberCommand command) {
        return addOrganizationMemberUseCase.execute(command);
    }

    public OrganizationMemberResult acceptInvitation(String token, Long userId) {
        var command = AcceptInvitationCommand.builder()
                .token(token)
                .userId(userId)
                .build();
        return acceptInvitationUseCase.execute(command);
    }

    public OrganizationMemberResult rejectInvitation(String token, Long userId) {
        var command = RejectInvitationCommand.builder()
                .token(token)
                .userId(userId)
                .build();
        return rejectInvitationUseCase.execute(command);
    }
}
