package com.ute.ticket.event.domain.entity;

import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class EventCategory extends BaseDomain {

    private final Long eventId;
    private final Long categoryId;

    public static EventCategory create(Long eventId, Long categoryId) {
        if (eventId == null) {
            throw new DomainValidationException("Event id cannot be null.");
        }

        if (categoryId == null) {
            throw new DomainValidationException("Category id cannot be null.");
        }

        return EventCategory.builder()
                .eventId(eventId)
                .categoryId(categoryId)
                .build();
    }
}
