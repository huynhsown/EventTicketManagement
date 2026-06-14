package com.ute.ticket.organization.domain.entity;

import com.ute.ticket.organization.domain.enums.OrganizationStatus;
import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainConflictException;
import com.ute.ticket.shared.exception.DomainValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class Organization extends BaseDomain {

    private final Long id;
    private String name;
    private String slug;
    private String description;
    private String logoUrl;
    private String website;
    private OrganizationStatus status;
    private Long ownerId;

    public static Organization create(
            String name,
            String slug,
            String description,
            String logoUrl,
            String website,
            Long ownerId
    ) {
        if (name == null || name.isBlank()) {
            throw new DomainValidationException("Organization name cannot be blank.");
        }

        if (slug == null || slug.isBlank()) {
            throw new DomainValidationException("Organization slug cannot be blank.");
        }

        if (ownerId == null) {
            throw new DomainValidationException("Owner id cannot be null.");
        }


        return Organization.builder()
                .name(name.trim())
                .slug(slug.trim().toLowerCase())
                .description(description)
                .logoUrl(logoUrl)
                .website(website)
                .status(OrganizationStatus.PENDING)
                .ownerId(ownerId)
                .build();
    }

    public void rename(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainValidationException("Organization name cannot be blank.");
        }
        this.name = name;
    }

    public void changeDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new DomainValidationException("Organization description cannot be blank.");
        }
        this.description = description;
    }

    public void changeLogo(String logoUrl) {
        if (logoUrl == null || logoUrl.isBlank()) {
            throw new DomainValidationException("Organization logoUrl cannot be blank.");
        }
        this.logoUrl = logoUrl;
    }

    public void changeWebsite(String website) {
        if (website == null || website.isBlank()) {
            throw new DomainValidationException("Organization website cannot be blank.");
        }
        this.website = website;
    }

    public void activate() {
        if (this.status == OrganizationStatus.ACTIVE) {
            throw new DomainConflictException("Organization is already active");
        }

        if (this.status == OrganizationStatus.INACTIVE) {
            throw new DomainConflictException("Inactive organization cannot be activated");
        }

        this.status = OrganizationStatus.ACTIVE;
    }

    public void suspend() {
        if (status == OrganizationStatus.SUSPENDED) {
            throw new DomainConflictException("Organization is already suspended.");
        }

        if (status == OrganizationStatus.INACTIVE) {
            throw new DomainConflictException("Inactive organization cannot be suspended.");
        }

        status = OrganizationStatus.SUSPENDED;
    }

    public void deactivate() {
        if (status == OrganizationStatus.INACTIVE) {
            throw new DomainConflictException("Organization is already inactive.");
        }

        status = OrganizationStatus.INACTIVE;
    }

}
