package com.ute.ticket.event.infrastructure.persistence.jpa.repository;

import com.ute.ticket.event.infrastructure.persistence.jpa.entity.EventCategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface EventCategoryJpaRepository
        extends JpaRepository<EventCategoryJpaEntity, EventCategoryJpaEntity.EventCategoryId> {

    List<EventCategoryJpaEntity> findByEventId(Long eventId);
    List<EventCategoryJpaEntity> findByCategoryId(Long categoryId);
    boolean existsByCategoryId(Long categoryId);
    boolean existsByEventIdAndCategoryId(Long eventId, Long categoryId);
    void deleteByEventIdAndCategoryIdIn(Long eventId, Collection<Long> categoryIds);
    long countByEventId(Long eventId);
    long countByCategoryId(Long categoryId);
}
