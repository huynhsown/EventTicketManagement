package com.ute.ticket.event.application.port.out;

import com.ute.ticket.event.domain.entity.EventCategory;

import java.util.List;

public interface EventCategoryRepository {
    EventCategory save(EventCategory eventCategory);
    List<EventCategory> saveAll(List<EventCategory> eventCategories);
    boolean existsByCategoryId(Long categoryId);
}
