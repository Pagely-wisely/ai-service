package com.pagely.aiservice.ai.application.port.out;

public interface DomainEventPublisher {

    void publish(Object event);
}
