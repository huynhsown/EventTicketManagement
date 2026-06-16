package com.ute.ticket.organization.application.service;

import com.ute.ticket.organization.application.command.ActivateOrganizationCommand;
import com.ute.ticket.organization.application.command.SuspendOrganizationCommand;
import com.ute.ticket.organization.application.port.in.ActivateOrganizationUseCase;
import com.ute.ticket.organization.application.port.in.SuspendOrganizationUseCase;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.organization.application.port.out.OrganizationRepository;
import com.ute.ticket.organization.application.result.OrganizationResult;
import com.ute.ticket.organization.domain.entity.Organization;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SuspendOrganizationService implements SuspendOrganizationUseCase {

    private final OrganizationRepository organizationRepository;

    @Override
    public OrganizationResult execute(SuspendOrganizationCommand cmd) {
        Organization organization = organizationRepository.findById(cmd.getOrganizationId())
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        organization.suspend();
        organization = organizationRepository.save(organization);

        return OrganizationResult.from(organization);
    }
}
