package com.ute.ticket.organization.application.service;

import com.ute.ticket.organization.application.command.TransferOwnershipCommand;
import com.ute.ticket.organization.application.port.in.TransferOwnershipUseCase;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.organization.application.port.out.OrganizationRepository;
import com.ute.ticket.organization.application.result.OrganizationResult;
import com.ute.ticket.organization.domain.entity.Organization;
import com.ute.ticket.organization.domain.entity.OrganizationMember;
import com.ute.ticket.shared.exception.ConflictException;
import com.ute.ticket.shared.exception.ForbiddenException;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferOwnershipService implements TransferOwnershipUseCase {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    @Override
    public OrganizationResult execute(TransferOwnershipCommand cmd) {
        Organization organization = organizationRepository.findById(cmd.getOrganizationId())
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        if (!organization.isActive()) {
            throw new ConflictException("Organization isn't active");
        }

        OrganizationMember actor = organizationMemberRepository.findById(cmd.getOrganizationId(), cmd.getUserId())
                .orElseThrow(() -> new ForbiddenException("Only the current owner can transfer ownership"));

        if (!actor.isOwner()) {
            throw new ForbiddenException("Only the current owner can transfer ownership");
        }

        OrganizationMember target = organizationMemberRepository.findById(cmd.getOrganizationId(), cmd.getTargetUserId())
                .orElseThrow(() -> new NotFoundException("Target member not found in this organization"));

        if (target.isOwner()) {
            throw new ConflictException("Target member is already the owner.");
        }

        if (!target.isActive()) {
            throw new ConflictException("Target member must be active to become the owner.");
        }

        organization.transferOwnership(cmd.getTargetUserId());
        actor.demoteFromOwner();
        target.promoteToOwner();

        organization = organizationRepository.save(organization);
        organizationMemberRepository.save(actor);
        organizationMemberRepository.save(target);

        return OrganizationResult.from(organization);
    }
}
