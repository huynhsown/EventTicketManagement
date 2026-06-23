package com.ute.ticket.event.application.port.out;

public interface EventCategoryRepository {
    boolean existsByCategoryId(Long categoryId);
}
