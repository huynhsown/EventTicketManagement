package com.ute.ticket.organization.application.service;

import com.ute.ticket.organization.application.command.ChangeOrganizationSlugCommand;
import com.ute.ticket.organization.application.port.in.ChangeOrganizationSlugUseCase;
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
public class ChangeOrganizationSlugService implements ChangeOrganizationSlugUseCase {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    @Override
    public OrganizationResult execute(ChangeOrganizationSlugCommand cmd) {
        Organization organization = organizationRepository.findById(cmd.getOrganizationId())
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        if (!organizationMemberRepository.existsAdminById(cmd.getOrganizationId(), cmd.getUserId())) {
            throw new ForbiddenException("Only organization owner or admin can update the organization profile");
        }

        String newSlug = Organization.normalizeSlug(cmd.getNewSlug());

        if (organizationRepository.existsBySlugAndIdNot(newSlug, organization.getId())) {
            throw new ConflictException("This slug is already in use.");
        }

        organization.changeSlug(newSlug);
        organization = organizationRepository.save(organization);

        return OrganizationResult.from(organization);
    }
}
