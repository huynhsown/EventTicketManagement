package com.ute.ticket.event.application.port.out;

import com.ute.ticket.event.domain.entity.Category;
import com.ute.ticket.event.domain.enums.CategoryStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findByIdsIn(Collection<Long> ids);
    boolean existsAllAssignable(Set<Long> ids);
    boolean existsByNameIgnoreCase(String name);
    boolean existsBySlug(String slug);
}
