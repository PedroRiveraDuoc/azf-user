package com.function.application.usecase;

import com.function.domain.model.DomainEvent;
import com.infrastructure.eventgrid.EventGridPublisher;
import java.util.logging.Logger;

/**
 * Caso de uso para publicar eventos de dominio en Event Grid.
 */
public class PublishDomainEventUseCase {
    private final Logger logger = Logger.getLogger(PublishDomainEventUseCase.class.getName());
    private final EventGridPublisher publisher;

    /**
     * Constructor que inicializa el publicador de eventos.
     * @param publisher Publicador de eventos Event Grid
     */
    public PublishDomainEventUseCase(EventGridPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Publica un evento de dominio en Event Grid.
     * @param event Evento de dominio a publicar
     */
    public void execute(DomainEvent event) {
        logger.info("[PublishDomainEventUseCase] Publicando evento: " + event.getType());
        try {
            publisher.publish(event);
            logger.info("Evento publicado correctamente en Event Grid. Tipo: " + event.getType());
        } catch (Exception e) {
            logger.severe("Error crítico al publicar evento en Event Grid. Tipo: " + event.getType() + ". Mensaje: " + e.getMessage());
            throw new RuntimeException("Error al publicar evento en Event Grid", e);
        }
    }
} 