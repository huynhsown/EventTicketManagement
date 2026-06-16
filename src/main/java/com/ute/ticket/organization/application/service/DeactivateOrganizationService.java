package com.ute.ticket.organization.application.service;

import com.ute.ticket.organization.application.command.DeactivateOrganizationCommand;
import com.ute.ticket.organization.application.port.in.DeactivateOrganizationUseCase;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.organization.application.port.out.OrganizationRepository;
import com.ute.ticket.organization.application.result.OrganizationResult;
import com.ute.ticket.organization.domain.entity.Organization;
import com.ute.ticket.organization.domain.entity.OrganizationMember;
import com.ute.ticket.shared.exception.ForbiddenException;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeactivateOrganizationService implements DeactivateOrganizationUseCase {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    @Override
    public OrganizationResult execute(DeactivateOrganizationCommand cmd) {
        Organization organization = organizationRepository.findById(cmd.getOrganizationId())
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        OrganizationMember member = organizationMemberRepository.findById(cmd.getOrganizationId(), cmd.getUserId())
                .orElseThrow(() -> new ForbiddenException("Only organization owner can deactivate"));

        if (!member.isOwner()) {
            throw new ForbiddenException("Only organization owner can deactivate");
        }

        organization.deactivate();
        organization = organizationRepository.save(organization);

        // Will implement deactivate event, ticket, ... at here

        return OrganizationResult.from(organization);
    }
}
