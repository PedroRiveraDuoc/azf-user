package com.infrastructure.eventgrid;

import com.function.domain.model.DomainEvent;

public interface EventGridPublisher {
    void publish(DomainEvent event);
} 