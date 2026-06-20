package com.ute.ticket.organization.infrastructure.persistence.adapter;

import com.ute.ticket.organization.application.port.out.OrganizationInvitationRepository;
import com.ute.ticket.organization.domain.entity.OrganizationInvitation;
import com.ute.ticket.organization.domain.enums.InvitationStatus;
import com.ute.ticket.organization.infrastructure.persistence.jpa.entity.OrganizationInvitationJpaEntity;
import com.ute.ticket.organization.infrastructure.persistence.jpa.mapper.OrganizationInvitationMapper;
import com.ute.ticket.organization.infrastructure.persistence.jpa.repository.OrganizationInvitationJpaRepository;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrganizationInvitationRepositoryAdapter implements OrganizationInvitationRepository {

    private final OrganizationInvitationJpaRepository organizationInvitationJpaRepository;
    private final OrganizationInvitationMapper organizationInvitationMapper;

    @Override
    public OrganizationInvitation save(OrganizationInvitation invitation) {
        OrganizationInvitationJpaEntity jpaEntity;
        if (invitation.getVersion() == null) {
            jpaEntity = organizationInvitationMapper.toJpaEntity(invitation);
        } else {
            jpaEntity = organizationInvitationJpaRepository.findById(invitation.getId())
                    .orElseThrow(() -> new NotFoundException("Organization invitation not found"));
            organizationInvitationMapper.updateEntity(jpaEntity, invitation);
        }
        OrganizationInvitationJpaEntity saved = organizationInvitationJpaRepository.save(jpaEntity);
        return organizationInvitationMapper.toDomain(saved);
    }

    @Override
    public Optional<OrganizationInvitation> findById(UUID id) {
        return organizationInvitationJpaRepository.findById(id)
                .map(organizationInvitationMapper::toDomain);
    }

    @Override
    public Optional<OrganizationInvitation> findByToken(String token) {
        return organizationInvitationJpaRepository.findByToken(token)
                .map(organizationInvitationMapper::toDomain);
    }

    @Override
    public List<OrganizationInvitation> findByOrganizationId(Long organizationId) {
        return organizationInvitationJpaRepository.findByOrganizationId(organizationId)
                .stream()
                .map(organizationInvitationMapper::toDomain)
                .toList();
    }

    @Override
    public List<OrganizationInvitation> findByOrganizationIdAndStatus(Long organizationId, InvitationStatus status) {
        return organizationInvitationJpaRepository.findByOrganizationIdAndStatus(organizationId, status)
                .stream()
                .map(organizationInvitationMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<OrganizationInvitation> findByOrganizationIdAndEmail(Long organizationId, String email) {
        return organizationInvitationJpaRepository.findByOrganizationIdAndEmail(organizationId, email)
                .map(organizationInvitationMapper::toDomain);
    }

    @Override
    public boolean existsByToken(String token) {
        return organizationInvitationJpaRepository.existsByToken(token);
    }

    @Override
    public void deleteById(UUID id) {
        organizationInvitationJpaRepository.deleteById(id);
    }
}
