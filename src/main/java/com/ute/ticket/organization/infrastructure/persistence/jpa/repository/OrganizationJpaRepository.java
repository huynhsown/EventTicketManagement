package com.ute.ticket.organization.infrastructure.persistence.jpa.repository;

import com.ute.ticket.organization.domain.enums.OrganizationStatus;
import com.ute.ticket.organization.infrastructure.persistence.jpa.entity.OrganizationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationJpaRepository extends JpaRepository<OrganizationJpaEntity, Long> {
    Optional<OrganizationJpaEntity> findBySlug(String slug);
    List<OrganizationJpaEntity> findByOwnerId(Long ownerId);
    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, Long id);
    boolean existsByIdAndStatus(Long id, OrganizationStatus status);
}
