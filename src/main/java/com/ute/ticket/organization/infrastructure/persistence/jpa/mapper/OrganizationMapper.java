package com.ute.ticket.organization.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.organization.domain.entity.Organization;
import com.ute.ticket.organization.infrastructure.persistence.jpa.entity.OrganizationJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {

    public Organization toDomain(OrganizationJpaEntity entity) {
        return Organization.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .logoUrl(entity.getLogoUrl())
                .website(entity.getWebsite())
                .status(entity.getStatus())
                .ownerId(entity.getOwnerId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();
    }

    public OrganizationJpaEntity toJpaEntity(Organization domain) {
        return OrganizationJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .slug(domain.getSlug())
                .description(domain.getDescription())
                .logoUrl(domain.getLogoUrl())
                .website(domain.getWebsite())
                .status(domain.getStatus())
                .ownerId(domain.getOwnerId())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deletedAt(domain.getDeletedAt())
                .version(domain.getVersion())
                .build();
    }

    public void updateEntity(OrganizationJpaEntity entity, Organization domain) {
        entity.setName(domain.getName());
        entity.setSlug(domain.getSlug());
        entity.setDescription(domain.getDescription());
        entity.setLogoUrl(domain.getLogoUrl());
        entity.setWebsite(domain.getWebsite());
        entity.setStatus(domain.getStatus());
        entity.setOwnerId(domain.getOwnerId());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setVersion(domain.getVersion());
    }
}
