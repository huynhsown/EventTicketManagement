package com.ute.ticket.organization.application.service;

import com.ute.ticket.organization.application.command.UpdateOrganizationProfileCommand;
import com.ute.ticket.organization.application.port.in.UpdateOrganizationProfileUseCase;
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
public class UpdateOrganizationProfileService implements UpdateOrganizationProfileUseCase {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    @Override
    public OrganizationResult execute(UpdateOrganizationProfileCommand cmd) {
        Organization organization = organizationRepository.findById(cmd.getOrganizationId())
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        if (!organizationMemberRepository.existsAdminById(cmd.getOrganizationId(), cmd.getUserId())) {
            throw new ForbiddenException("Only organization owner or admin can update the organization profile");
        }

        organization.updateProfile(
                cmd.getName(),
                cmd.getDescription(),
                cmd.getLogoUrl(),
                cmd.getWebsite()
        );
        organization = organizationRepository.save(organization);

        return OrganizationResult.from(organization);
    }
}
