package com.ute.ticket.shared.infrastructure.messaging;

import com.ute.ticket.shared.application.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    private static final String TOPIC_PREFIX = "event.";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(Object domainEvent) {
        publish(resolveTopic(domainEvent), domainEvent);
    }

    @Override
    public void publish(String topic, Object payload) {
        kafkaTemplate.send(topic, payload).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish event to topic [{}]: {}", topic, payload, ex);
            } else {
                log.info("Published event to topic [{}] at offset {}", topic,
                        result.getRecordMetadata().offset());
            }
        });
    }

    private String resolveTopic(Object domainEvent) {
        String typeName = domainEvent.getClass().getSimpleName();
        return TOPIC_PREFIX + toKebabCase(typeName);
    }

    private String toKebabCase(String value) {
        return value.replaceAll("([a-z0-9])([A-Z])", "$1-$2").toLowerCase(Locale.ROOT);
    }
}
