package com.ute.ticket.organization.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.organization.domain.entity.OrganizationInvitation;
import com.ute.ticket.organization.infrastructure.persistence.jpa.entity.OrganizationInvitationJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class OrganizationInvitationMapper {

    public OrganizationInvitation toDomain(OrganizationInvitationJpaEntity entity) {
        return OrganizationInvitation.builder()
                .id(entity.getId())
                .organizationId(entity.getOrganizationId())
                .email(entity.getEmail())
                .role(entity.getRole())
                .invitedBy(entity.getInvitedBy())
                .token(entity.getToken())
                .status(entity.getStatus())
                .expiresAt(entity.getExpiresAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();
    }

    public OrganizationInvitationJpaEntity toJpaEntity(OrganizationInvitation domain) {
        return OrganizationInvitationJpaEntity.builder()
                .id(domain.getId())
                .organizationId(domain.getOrganizationId())
                .email(domain.getEmail())
                .role(domain.getRole())
                .invitedBy(domain.getInvitedBy())
                .token(domain.getToken())
                .status(domain.getStatus())
                .expiresAt(domain.getExpiresAt())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deletedAt(domain.getDeletedAt())
                .version(domain.getVersion())
                .build();
    }

    public void updateEntity(OrganizationInvitationJpaEntity entity, OrganizationInvitation domain) {
        entity.setEmail(domain.getEmail());
        entity.setRole(domain.getRole());
        entity.setStatus(domain.getStatus());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setVersion(domain.getVersion());
    }
}
