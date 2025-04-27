package com.application.usecase;

import com.domain.model.DomainEvent;
import com.infrastructure.eventgrid.EventGridPublisher;

public class PublishDomainEventUseCase {
    private final EventGridPublisher publisher;

    public PublishDomainEventUseCase(EventGridPublisher publisher) {
        this.publisher = publisher;
    }

    public void execute(DomainEvent event) {
        publisher.publish(event);
    }
} 