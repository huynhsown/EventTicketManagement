package com.ute.ticket.organization.infrastructure.persistence.jpa.repository;

import com.ute.ticket.organization.domain.enums.MemberRole;
import com.ute.ticket.organization.domain.enums.MemberStatus;
import com.ute.ticket.organization.infrastructure.persistence.jpa.entity.OrganizationMemberJpaEntity;
import com.ute.ticket.organization.infrastructure.persistence.jpa.entity.OrganizationMemberJpaEntity.OrganizationMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface OrganizationMemberJpaRepository extends JpaRepository<OrganizationMemberJpaEntity, OrganizationMemberId> {
    List<OrganizationMemberJpaEntity> findByOrganizationId(Long organizationId);
    List<OrganizationMemberJpaEntity> findByOrganizationIdAndStatus(Long organizationId, MemberStatus status);
    List<OrganizationMemberJpaEntity> findByUserId(Long userId);
    boolean existsByOrganizationIdAndUserIdAndRoleIn(Long organizationId, Long userId, Collection<MemberRole> roles);
}
