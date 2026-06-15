package com.ute.ticket.organization.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.organization.domain.entity.OrganizationMember;
import com.ute.ticket.organization.infrastructure.persistence.jpa.entity.OrganizationMemberJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMemberMapper {

    public OrganizationMember toDomain(OrganizationMemberJpaEntity entity) {
        return OrganizationMember.builder()
                .organizationId(entity.getOrganizationId())
                .userId(entity.getUserId())
                .role(entity.getRole())
                .status(entity.getStatus())
                .joinedAt(entity.getJoinedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();
    }

    public OrganizationMemberJpaEntity toJpaEntity(OrganizationMember domain) {
        return OrganizationMemberJpaEntity.builder()
                .organizationId(domain.getOrganizationId())
                .userId(domain.getUserId())
                .role(domain.getRole())
                .status(domain.getStatus())
                .joinedAt(domain.getJoinedAt())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deletedAt(domain.getDeletedAt())
                .version(domain.getVersion())
                .build();
    }

    public void updateEntity(OrganizationMemberJpaEntity entity, OrganizationMember domain) {
        entity.setRole(domain.getRole());
        entity.setStatus(domain.getStatus());
        entity.setJoinedAt(domain.getJoinedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setVersion(domain.getVersion());
    }
}
