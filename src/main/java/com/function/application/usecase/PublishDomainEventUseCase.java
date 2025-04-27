package com.function.application.usecase;

import com.function.domain.model.DomainEvent;
import com.infrastructure.eventgrid.EventGridPublisher;
import java.util.logging.Logger;

public class PublishDomainEventUseCase {
    private final Logger logger = Logger.getLogger(PublishDomainEventUseCase.class.getName());
    private final EventGridPublisher publisher;

    public PublishDomainEventUseCase(EventGridPublisher publisher) {
        this.publisher = publisher;
    }

    public void execute(DomainEvent event) {
        logger.info("Publicando evento: " + event.getType());
        try {
            publisher.publish(event);
            logger.info("Evento publicado correctamente en Event Grid");
        } catch (Exception e) {
            logger.severe("Error al publicar evento en Event Grid: " + e.getMessage());
            throw new RuntimeException("Error al publicar evento en Event Grid", e);
        }
    }
} 