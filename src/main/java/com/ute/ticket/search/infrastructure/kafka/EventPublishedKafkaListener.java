package com.ute.ticket.search.infrastructure.kafka;

import com.ute.ticket.event.domain.event.EventPublished;
import com.ute.ticket.search.application.port.out.EventIndexer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublishedKafkaListener {

    private final EventIndexer eventIndexer;

    @KafkaListener(topics = "event.event-published", groupId = "search")
    public void onEventPublished(EventPublished eventPublished) {
        eventIndexer.updateStatus(
                eventPublished.eventId(),
                eventPublished.status().name(),
                eventPublished.publishedAt()
        );
        log.info("Updated status of event [{}] to [{}] in search", eventPublished.eventId(), eventPublished.status());
    }
}
