package com.ute.ticket.organization.infrastructure.persistence.jpa.repository;

import com.ute.ticket.organization.domain.enums.InvitationStatus;
import com.ute.ticket.organization.infrastructure.persistence.jpa.entity.OrganizationInvitationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationInvitationJpaRepository extends JpaRepository<OrganizationInvitationJpaEntity, UUID> {
    Optional<OrganizationInvitationJpaEntity> findByToken(String token);
    List<OrganizationInvitationJpaEntity> findByOrganizationId(Long organizationId);
    List<OrganizationInvitationJpaEntity> findByOrganizationIdAndStatus(Long organizationId, InvitationStatus status);
    Optional<OrganizationInvitationJpaEntity> findByOrganizationIdAndEmail(Long organizationId, String email);
    boolean existsByToken(String token);
}
