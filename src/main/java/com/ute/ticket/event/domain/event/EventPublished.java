package com.ute.ticket.event.domain.event;

import com.ute.ticket.event.domain.enums.EventStatus;

import java.time.Instant;

public record EventPublished(Long eventId, EventStatus status, Instant publishedAt) {
}
