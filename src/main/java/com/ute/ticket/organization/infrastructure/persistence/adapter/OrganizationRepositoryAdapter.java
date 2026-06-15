package com.ute.ticket.organization.infrastructure.persistence.adapter;

import com.ute.ticket.organization.application.port.out.OrganizationRepository;
import com.ute.ticket.organization.domain.entity.Organization;
import com.ute.ticket.organization.infrastructure.persistence.jpa.entity.OrganizationJpaEntity;
import com.ute.ticket.organization.infrastructure.persistence.jpa.mapper.OrganizationMapper;
import com.ute.ticket.organization.infrastructure.persistence.jpa.repository.OrganizationJpaRepository;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrganizationRepositoryAdapter implements OrganizationRepository {

    private final OrganizationJpaRepository organizationJpaRepository;
    private final OrganizationMapper organizationMapper;

    @Override
    public Organization save(Organization organization) {
        OrganizationJpaEntity jpaEntity;
        if (organization.getId() == null) {
            jpaEntity = organizationMapper.toJpaEntity(organization);
        } else {
            jpaEntity = organizationJpaRepository.findById(organization.getId())
                    .orElseThrow(() -> new NotFoundException("Organization not found"));
            organizationMapper.updateEntity(jpaEntity, organization);
        }
        OrganizationJpaEntity saved = organizationJpaRepository.save(jpaEntity);
        return organizationMapper.toDomain(saved);
    }

    @Override
    public Optional<Organization> findById(Long id) {
        return organizationJpaRepository.findById(id)
                .map(organizationMapper::toDomain);
    }

    @Override
    public Optional<Organization> findBySlug(String slug) {
        return organizationJpaRepository.findBySlug(slug)
                .map(organizationMapper::toDomain);
    }

    @Override
    public List<Organization> findByOwnerId(Long ownerId) {
        return organizationJpaRepository.findByOwnerId(ownerId)
                .stream()
                .map(organizationMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsBySlug(String slug) {
        return organizationJpaRepository.existsBySlug(slug);
    }

    @Override
    public void deleteById(Long id) {
        organizationJpaRepository.deleteById(id);
    }
}
