package com.ute.ticket.event.application.result;

import com.ute.ticket.event.domain.entity.Category;
import com.ute.ticket.event.domain.enums.CategoryStatus;

public record CategoryResult(
        Long id,
        String name,
        String slug,
        String description,
        Integer displayOrder,
        CategoryStatus status
) {
    public static CategoryResult from(Category category) {
        return new CategoryResult(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getDisplayOrder(),
                category.getStatus()
        );
    }
}
