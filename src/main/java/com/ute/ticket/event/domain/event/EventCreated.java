package com.ute.ticket.event.domain.event;

import java.util.Set;

public record EventCreated(Long eventId, Set<Long> categoryIds) {
}
