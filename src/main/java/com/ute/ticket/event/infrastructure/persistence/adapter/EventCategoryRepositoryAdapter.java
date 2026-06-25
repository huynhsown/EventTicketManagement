package com.ute.ticket.event.infrastructure.persistence.adapter;

import com.ute.ticket.event.application.port.out.EventCategoryRepository;
import com.ute.ticket.event.domain.entity.EventCategory;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.EventCategoryJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.mapper.EventCategoryMapper;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.EventCategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public boolean existsByCategoryId(Long categoryId) {
        return eventCategoryJpaRepository.existsByCategoryId(categoryId);
    }
}
