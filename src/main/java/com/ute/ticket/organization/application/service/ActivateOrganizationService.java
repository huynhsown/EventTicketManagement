package com.ute.ticket.organization.application.service;

import com.ute.ticket.organization.application.command.ActivateOrganizationCommand;
import com.ute.ticket.organization.application.port.in.ActivateOrganizationUseCase;
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
public class ActivateOrganizationService implements ActivateOrganizationUseCase {

    private final OrganizationRepository organizationRepository;

    @Override
    public OrganizationResult execute(ActivateOrganizationCommand cmd) {
        Organization organization = organizationRepository.findById(cmd.getOrganizationId())
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        organization.activate();
        organization = organizationRepository.save(organization);

        return OrganizationResult.from(organization);
    }
}
