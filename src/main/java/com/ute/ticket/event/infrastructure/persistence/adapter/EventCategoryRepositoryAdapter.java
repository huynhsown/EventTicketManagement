package com.ute.ticket.event.infrastructure.persistence.adapter;

import com.ute.ticket.event.application.port.out.EventCategoryRepository;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.EventCategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventCategoryRepositoryAdapter implements EventCategoryRepository {

    private final EventCategoryJpaRepository eventCategoryJpaRepository;

    @Override
    public boolean existsByCategoryId(Long categoryId) {
        return eventCategoryJpaRepository.existsByCategoryId(categoryId);
    }
}
