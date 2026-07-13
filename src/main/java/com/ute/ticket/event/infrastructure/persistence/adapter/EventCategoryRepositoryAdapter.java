package com.ute.ticket.event.infrastructure.persistence.adapter;

import com.ute.ticket.event.application.port.out.EventCategoryRepository;
import com.ute.ticket.event.domain.entity.EventCategory;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.EventCategoryJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.mapper.EventCategoryMapper;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.EventCategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class EventCategoryRepositoryAdapter implements EventCategoryRepository {

    private final EventCategoryJpaRepository eventCategoryJpaRepository;
    private final EventCategoryMapper eventCategoryMapper;

    @Override
    public EventCategory save(EventCategory eventCategory) {
        EventCategoryJpaEntity entity = eventCategoryMapper.toJpaEntity(eventCategory);
        EventCategoryJpaEntity saved = eventCategoryJpaRepository.save(entity);
        return eventCategoryMapper.toDomain(saved);
    }

    @Override
    public List<EventCategory> saveAll(List<EventCategory> eventCategories) {
        List<EventCategoryJpaEntity> entities = eventCategories.stream()
                .map(eventCategoryMapper::toJpaEntity)
                .toList();
        return eventCategoryJpaRepository.saveAll(entities)
                .stream()
                .map(eventCategoryMapper::toDomain)
                .toList();
    }

    @Override
    public List<EventCategory> findByEventId(Long eventId) {
        return eventCategoryJpaRepository.findByEventId(eventId)
                .stream()
                .map(eventCategoryMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteIn(Long eventId, Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }
        eventCategoryJpaRepository.deleteByEventIdAndCategoryIdIn(eventId, categoryIds);
    }

    @Override
    public boolean existsByCategoryId(Long categoryId) {
        return eventCategoryJpaRepository.existsByCategoryId(categoryId);
    }
}
