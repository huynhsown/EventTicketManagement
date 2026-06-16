package com.ute.ticket.organization.application.service;

import com.ute.ticket.organization.application.port.in.GetOrganizationUseCase;
import com.ute.ticket.organization.application.port.out.OrganizationRepository;
import com.ute.ticket.organization.application.result.OrganizationResult;
import com.ute.ticket.organization.domain.entity.Organization;
import com.ute.ticket.organization.domain.enums.OrganizationStatus;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetOrganizationService implements GetOrganizationUseCase {

    private final OrganizationRepository organizationRepository;

    @Override
    public OrganizationResult execute(String slug) {
        Organization organization = organizationRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        if (organization.getStatus() != OrganizationStatus.ACTIVE) {
            throw new NotFoundException("Organization not found");
        }

        return OrganizationResult.from(organization);
    }
}
