package com.ute.ticket.organization.application.port.out;

import com.ute.ticket.organization.domain.entity.OrganizationInvitation;
import com.ute.ticket.organization.domain.enums.InvitationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationInvitationRepository {
    OrganizationInvitation save(OrganizationInvitation invitation);
    Optional<OrganizationInvitation> findById(UUID id);
    Optional<OrganizationInvitation> findByToken(String token);
    List<OrganizationInvitation> findByOrganizationId(Long organizationId);
    List<OrganizationInvitation> findByOrganizationIdAndStatus(Long organizationId, InvitationStatus status);
    Optional<OrganizationInvitation> findByOrganizationIdAndEmail(Long organizationId, String email);
    boolean existsByToken(String token);
    void deleteById(UUID id);
}
