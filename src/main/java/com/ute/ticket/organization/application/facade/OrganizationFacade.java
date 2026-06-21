package com.ute.ticket.organization.application.facade;

import com.ute.ticket.organization.application.command.AcceptInvitationCommand;
import com.ute.ticket.organization.application.command.ActivateOrganizationCommand;
import com.ute.ticket.organization.application.command.AddOrganizationMemberCommand;
import com.ute.ticket.organization.application.command.CreateOrganizationCommand;
import com.ute.ticket.organization.application.command.DeactivateOrganizationCommand;
import com.ute.ticket.organization.application.command.SuspendOrganizationCommand;
import com.ute.ticket.organization.application.port.in.AcceptInvitationUseCase;
import com.ute.ticket.organization.application.port.in.ActivateOrganizationUseCase;
import com.ute.ticket.organization.application.port.in.InviteOrganizationMemberUseCase;
import com.ute.ticket.organization.application.port.in.CreateOrganizationUseCase;
import com.ute.ticket.organization.application.port.in.DeactivateOrganizationUseCase;
import com.ute.ticket.organization.application.port.in.GetOrganizationUseCase;
import com.ute.ticket.organization.application.port.in.SuspendOrganizationUseCase;
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
    private final InviteOrganizationMemberUseCase addOrganizationMemberUseCase;
    private final AcceptInvitationUseCase acceptInvitationUseCase;

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
}
