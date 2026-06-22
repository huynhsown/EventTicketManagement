package com.ute.ticket.event.domain.entity;

import com.ute.ticket.event.domain.enums.CategoryStatus;
import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainConflictException;
import com.ute.ticket.shared.exception.DomainValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class Category extends BaseDomain {

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_SLUG_LENGTH = 100;

    private final Long id;
    private String name;
    private String slug;
    private String description;
    private Integer displayOrder;
    private CategoryStatus status;

    public static Category create(
            String name,
            String slug,
            String description,
            Integer displayOrder
    ) {
        validateName(name);
        validateSlug(slug);

        return Category.builder()
                .name(name.trim())
                .slug(slug.trim().toLowerCase())
                .description(description)
                .displayOrder(displayOrder)
                .status(CategoryStatus.ACTIVE)
                .build();
    }

    public void changeDescription(String description) {
        ensureNotArchived();
        this.description = description;
    }

    public void rename(String name, String slug) {
        ensureNotArchived();
        validateName(name);
        validateSlug(slug);
        this.name = name.trim();
        this.slug = slug.trim().toLowerCase();
    }

    public void changeDisplayOrder(Integer displayOrder) {
        ensureNotArchived();
        this.displayOrder = displayOrder;
    }

    public void archive() {
        if (status == CategoryStatus.ARCHIVED) {
            throw new DomainConflictException("Category is already archived.");
        }

        if (status == CategoryStatus.DELETED) {
            throw new DomainConflictException("Deleted category cannot be archived.");
        }

        status = CategoryStatus.ARCHIVED;
    }

    public void restore() {
        if (status != CategoryStatus.ARCHIVED) {
            throw new DomainConflictException("Only archived categories can be restored.");
        }

        status = CategoryStatus.ACTIVE;
    }

    public boolean isAssignable() {
        return status == CategoryStatus.ACTIVE;
    }

    private void ensureNotArchived() {
        if (status == CategoryStatus.ARCHIVED) {
            throw new DomainConflictException("Archived category cannot be modified.");
        }

        if (status == CategoryStatus.DELETED) {
            throw new DomainConflictException("Deleted category cannot be modified.");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainValidationException("Category name cannot be blank.");
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new DomainValidationException("Category name must not exceed " + MAX_NAME_LENGTH + " characters.");
        }
    }

    private static void validateSlug(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new DomainValidationException("Category slug cannot be blank.");
        }

        if (slug.length() > MAX_SLUG_LENGTH) {
            throw new DomainValidationException("Category slug must not exceed " + MAX_SLUG_LENGTH + " characters.");
        }

        if (!slug.matches("^[a-z0-9-]+$")) {
            throw new DomainValidationException("Category slug must be lowercase alphanumeric with hyphens only.");
        }
    }
}
