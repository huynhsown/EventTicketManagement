package com.ute.ticket.search.application.port.out;

import com.ute.ticket.search.infrastructure.elasticsearch.document.EventDocument;

import java.time.Instant;

public interface EventIndexer {

    void index(EventDocument document);

    void updateStatus(Long eventId, String status, Instant publishedAt);

    void updateVenue(Long eventId, Long venueId, String venueName, String venueCity);

    void delete(Long eventId);
}
