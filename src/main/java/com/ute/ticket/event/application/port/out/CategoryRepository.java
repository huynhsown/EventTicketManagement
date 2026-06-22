package com.ute.ticket.event.application.port.out;

import com.ute.ticket.event.domain.entity.Category;

public interface CategoryRepository {
    Category save(Category category);
    boolean existsByNameIgnoreCase(String name);
    boolean existsBySlug(String slug);
}
