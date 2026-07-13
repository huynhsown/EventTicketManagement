package com.ute.ticket.event.application.port.out;

import com.ute.ticket.event.domain.entity.EventCategory;

import java.util.List;
import java.util.Set;

public interface EventCategoryRepository {
    EventCategory save(EventCategory eventCategory);
    List<EventCategory> saveAll(List<EventCategory> eventCategories);
    List<EventCategory> findByEventId(Long eventId);
    void deleteIn(Long eventId, Set<Long> categoryIds);
    boolean existsByCategoryId(Long categoryId);
}
