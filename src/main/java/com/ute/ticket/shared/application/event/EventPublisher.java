package com.ute.ticket.shared.application.event;

public interface EventPublisher {

    void publish(Object domainEvent);

    void publish(String topic, Object payload);
}
