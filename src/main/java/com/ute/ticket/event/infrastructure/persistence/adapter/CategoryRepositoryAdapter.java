package com.ute.ticket.event.infrastructure.persistence.adapter;

import com.ute.ticket.event.application.port.out.CategoryRepository;
import com.ute.ticket.event.domain.entity.Category;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.CategoryJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.mapper.CategoryMapper;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.CategoryJpaRepository;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category save(Category category) {
        CategoryJpaEntity jpaEntity;
        if (category.getId() == null || category.getVersion() == null) {
            jpaEntity = categoryMapper.toJpaEntity(category);
        } else {
            jpaEntity = categoryJpaRepository.findById(category.getId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            categoryMapper.updateEntity(jpaEntity, category);
        }
        CategoryJpaEntity saved = categoryJpaRepository.save(jpaEntity);
        return categoryMapper.toDomain(saved);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return categoryJpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return categoryJpaRepository.existsBySlug(slug);
    }
}
