package com.ute.ticket.organization.application.port.out;

import com.ute.ticket.organization.domain.entity.Organization;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository {
    Organization save(Organization organization);
    Optional<Organization> findById(Long id);
    Optional<Organization> findBySlug(String slug);
    List<Organization> findByOwnerId(Long ownerId);
    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, Long id);
    boolean existsById(Long id);
    boolean existsActiveById(Long id);
    void deleteById(Long id);
}
