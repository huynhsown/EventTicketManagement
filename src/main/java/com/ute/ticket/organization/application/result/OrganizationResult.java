package com.ute.ticket.organization.application.result;

import com.ute.ticket.organization.domain.entity.Organization;
import com.ute.ticket.organization.domain.enums.OrganizationStatus;

public record OrganizationResult(
        Long id,
        String name,
        String slug,
        String description,
        String logoUrl,
        String website,
        OrganizationStatus status,
        Long ownerId
) {
    public static OrganizationResult from(Organization organization) {
        return new OrganizationResult(
                organization.getId(),
                organization.getName(),
                organization.getSlug(),
                organization.getDescription(),
                organization.getLogoUrl(),
                organization.getWebsite(),
                organization.getStatus(),
                organization.getOwnerId()
        );
    }
}
