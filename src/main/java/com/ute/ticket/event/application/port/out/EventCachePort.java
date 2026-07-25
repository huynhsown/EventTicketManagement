package com.ute.ticket.event.application.port.out;

import com.ute.ticket.event.application.result.EventDetailResult;

public interface EventCachePort {
    EventDetailResult findBySlug(String slug);
    void save(EventDetailResult result);
    void evict(String slug);
}
