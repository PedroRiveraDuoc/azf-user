package com.infrastructure.eventgrid;

import com.domain.model.DomainEvent;

public interface EventGridPublisher {
    void publish(DomainEvent event);
} 