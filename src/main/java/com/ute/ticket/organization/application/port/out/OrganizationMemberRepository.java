package com.ute.ticket.organization.application.port.out;

import com.ute.ticket.organization.domain.entity.OrganizationMember;
import com.ute.ticket.organization.domain.enums.MemberStatus;

import java.util.List;
import java.util.Optional;

public interface OrganizationMemberRepository {
    OrganizationMember save(OrganizationMember member);
    Optional<OrganizationMember> findById(Long organizationId, Long userId);
    List<OrganizationMember> findByOrganizationId(Long organizationId);
    List<OrganizationMember> findByOrganizationIdAndStatus(Long organizationId, MemberStatus status);
    List<OrganizationMember> findByUserId(Long userId);
    boolean existsById(Long organizationId, Long userId);
    boolean existsAdminById(Long organizationId, Long userId);
    void deleteById(Long organizationId, Long userId);
}
