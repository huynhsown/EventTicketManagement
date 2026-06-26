package com.ute.ticket.organization.application.service;

import com.ute.ticket.organization.application.command.ChangeMemberRoleCommand;
import com.ute.ticket.organization.application.command.TransferOwnershipCommand;
import com.ute.ticket.organization.application.port.in.ChangeMemberRoleUseCase;
import com.ute.ticket.organization.application.port.in.TransferOwnershipUseCase;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.organization.application.port.out.OrganizationRepository;
import com.ute.ticket.organization.application.result.OrganizationMemberResult;
import com.ute.ticket.organization.domain.entity.Organization;
import com.ute.ticket.organization.domain.entity.OrganizationMember;
import com.ute.ticket.organization.domain.enums.MemberRole;
import com.ute.ticket.organization.domain.enums.MemberStatus;
import com.ute.ticket.shared.exception.ConflictException;
import com.ute.ticket.shared.exception.ForbiddenException;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChangeMemberRoleService implements ChangeMemberRoleUseCase {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final TransferOwnershipUseCase transferOwnershipUseCase;

    @Override
    public OrganizationMemberResult execute(ChangeMemberRoleCommand cmd) {
        Organization organization = organizationRepository.findById(cmd.getOrganizationId())
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        if (!organization.isActive()) {
            throw new ConflictException("Organization isn't active");
        }

        OrganizationMember actor = organizationMemberRepository.findById(cmd.getOrganizationId(), cmd.getUserId())
                .orElseThrow(() -> new ForbiddenException("Only the owner can change member roles"));

        if (!actor.isOwner()) {
            throw new ForbiddenException("Only the owner can change member roles");
        }

        OrganizationMember target = organizationMemberRepository.findById(cmd.getOrganizationId(), cmd.getTargetUserId())
                .orElseThrow(() -> new NotFoundException("Member not found in this organization"));

        if (target.isOwner()) {
            throw new ConflictException("Cannot change the owner's role.");
        }

        if (!target.isActive()) {
            throw new ConflictException("Cannot change role of a suspended member.");
        }

        if (cmd.getRole() == MemberRole.OWNER) {
            return delegateToTransferOwnership(cmd);
        }

        if (target.getRole() == cmd.getRole()) {
            return OrganizationMemberResult.from(target);
        }

        target.changeRole(cmd.getRole());
        target = organizationMemberRepository.save(target);

        return OrganizationMemberResult.from(target);
    }

    private OrganizationMemberResult delegateToTransferOwnership(ChangeMemberRoleCommand cmd) {
        var transferCommand = TransferOwnershipCommand.builder()
                .organizationId(cmd.getOrganizationId())
                .userId(cmd.getUserId())
                .targetUserId(cmd.getTargetUserId())
                .build();
        transferOwnershipUseCase.execute(transferCommand);

        OrganizationMember newOwner = organizationMemberRepository.findById(cmd.getOrganizationId(), cmd.getTargetUserId())
                .orElseThrow(() -> new NotFoundException("Member not found in this organization"));
        return OrganizationMemberResult.from(newOwner);
    }
}
