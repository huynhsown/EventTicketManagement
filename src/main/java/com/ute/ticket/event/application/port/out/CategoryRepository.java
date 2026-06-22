package com.ute.ticket.event.application.port.out;

import com.ute.ticket.event.domain.entity.Category;

import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    boolean existsByNameIgnoreCase(String name);
    boolean existsBySlug(String slug);
}
