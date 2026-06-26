package com.ute.ticket.organization.application.service;

import com.ute.ticket.organization.application.command.RemoveOrganizationMemberCommand;
import com.ute.ticket.organization.application.port.in.RemoveOrganizationMemberUseCase;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.organization.application.port.out.OrganizationRepository;
import com.ute.ticket.organization.application.result.OrganizationMemberResult;
import com.ute.ticket.organization.domain.entity.Organization;
import com.ute.ticket.organization.domain.entity.OrganizationMember;
import com.ute.ticket.organization.domain.enums.MemberStatus;
import com.ute.ticket.shared.exception.ConflictException;
import com.ute.ticket.shared.exception.ForbiddenException;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class RemoveOrganizationMemberService implements RemoveOrganizationMemberUseCase {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    @Override
    public OrganizationMemberResult execute(RemoveOrganizationMemberCommand cmd) {
        Organization organization = organizationRepository.findById(cmd.getOrganizationId())
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        if (!organization.isActive()) {
            throw new ConflictException("Organization isn't active");
        }

        if (!organizationMemberRepository.existsAdminById(cmd.getOrganizationId(), cmd.getUserId())) {
            throw new ForbiddenException("Only organization owner or admin can update the organization profile");
        }

        if (Objects.equals(cmd.getUserId(), cmd.getRemovedBy())) {
            throw new ForbiddenException(
                    "Members cannot remove themselves. Use the Leave Organization operation instead."
            );
        }

        OrganizationMember member = organizationMemberRepository.findById(cmd.getOrganizationId(), cmd.getUserId())
                .orElseThrow(() -> new NotFoundException("Member not found in this organization"));

        if (member.isOwner()) {
            throw new ConflictException("The owner cannot be removed. Transfer ownership first.");
        }

        if (member.getStatus() == MemberStatus.REMOVED) {
            return OrganizationMemberResult.from(member);
        }

        member.remove();
        member = organizationMemberRepository.save(member);

        // Will publish OrganizationMemberRemoved domain event at here

        return OrganizationMemberResult.from(member);
    }
}
