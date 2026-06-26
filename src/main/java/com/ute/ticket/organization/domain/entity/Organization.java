package com.ute.ticket.organization.domain.entity;

import com.ute.ticket.organization.domain.enums.OrganizationStatus;
import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainConflictException;
import com.ute.ticket.shared.exception.DomainValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.net.URI;
import java.net.URISyntaxException;

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

    public void changeSlug(String newSlug) {
        if (status != OrganizationStatus.ACTIVE) {
            throw new DomainConflictException("Cannot change the slug of a suspended or archived organization.");
        }

        String normalized = normalizeSlug(newSlug);

        if (normalized.equals(this.slug)) {
            return;
        }

        this.slug = normalized;
    }

    public static String normalizeSlug(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new DomainValidationException("Organization slug cannot be blank.");
        }

        String normalized = slug.trim().toLowerCase();

        if (!normalized.matches("^[a-z0-9]+(-[a-z0-9]+)*$")) {
            throw new DomainValidationException(
                    "Organization slug must be lowercase alphanumeric words separated by single hyphens."
            );
        }

        return normalized;
    }

    public void updateProfile(String name, String description, String logoUrl, String website) {
        if (status != OrganizationStatus.ACTIVE) {
            throw new DomainConflictException("Cannot update a suspended or archived organization.");
        }

        if (name != null) {
            if (name.isBlank()) {
                throw new DomainValidationException("Organization name cannot be blank.");
            }
            this.name = name.trim();
        }

        if (description != null) {
            this.description = description;
        }

        if (logoUrl != null) {
            validateUrl(logoUrl, "logoUrl");
            this.logoUrl = logoUrl;
        }

        if (website != null) {
            validateUrl(website, "website");
            this.website = website;
        }
    }

    public void transferOwnership(Long newOwnerId) {
        if (status != OrganizationStatus.ACTIVE) {
            throw new DomainConflictException("Only an active organization can transfer ownership.");
        }

        if (newOwnerId == null) {
            throw new DomainValidationException("New owner id cannot be null.");
        }

        if (newOwnerId.equals(this.ownerId)) {
            throw new DomainConflictException("Target member is already the owner.");
        }

        this.ownerId = newOwnerId;
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
        if (status != OrganizationStatus.ACTIVE) {
            throw new DomainConflictException(
                    "Only active organization can be suspended."
            );
        }
        status = OrganizationStatus.SUSPENDED;
    }

    public void deactivate() {
        if (status != OrganizationStatus.ACTIVE) {
            throw new DomainConflictException(
                    "Only active organization can be deactivated."
            );
        }
        status = OrganizationStatus.INACTIVE;
    }

    public boolean isActive() {
        return this.status == OrganizationStatus.ACTIVE;
    }

    private static void validateUrl(String url, String fieldName) {
        if (url == null || url.isBlank()) {
            throw new DomainValidationException(fieldName + " cannot be blank.");
        }

        try {
            URI uri = new URI(url);
            if (uri.getScheme() == null || uri.getHost() == null) {
                throw new DomainValidationException("Invalid " + fieldName + " URL format.");
            }
        } catch (URISyntaxException e) {
            throw new DomainValidationException("Invalid " + fieldName + " URL format.");
        }
    }

}
