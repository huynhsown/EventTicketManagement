package com.ute.ticket.event.infrastructure.persistence.jpa.repository;

import com.ute.ticket.event.domain.enums.CategoryStatus;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {
    Optional<CategoryJpaEntity> findBySlug(String slug);
    boolean existsByNameIgnoreCase(String name);
    boolean existsBySlug(String slug);
    List<CategoryJpaEntity> findByIdIn(Collection<Long> ids);
    long countByIdInAndStatus(Set<Long> ids, CategoryStatus status);
    List<CategoryJpaEntity> findByStatus(CategoryStatus status);
    List<CategoryJpaEntity> findByStatusOrderByDisplayOrderAsc(CategoryStatus status);

    Set<Long> id(Long id);
}
