package com.ute.ticket.search.infrastructure.rabbitmq;

import com.ute.ticket.event.domain.event.EventPublished;
import com.ute.ticket.search.application.port.out.EventIndexer;
import com.ute.ticket.shared.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublishedRabbitMQListener {

    private final EventIndexer eventIndexer;

    @RabbitListener(queues = RabbitMQConfig.EVENT_PUBLISHED_QUEUE)
    public void onEventPublished(EventPublished eventPublished) {
        eventIndexer.updateStatus(
                eventPublished.eventId(),
                eventPublished.status().name(),
                eventPublished.publishedAt()
        );
        log.info("Updated status of event [{}] to [{}] in search", eventPublished.eventId(), eventPublished.status());
    }
}
