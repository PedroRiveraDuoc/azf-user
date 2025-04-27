package com.infrastructure.eventgrid;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.azure.messaging.eventgrid.EventGridEvent;
import com.azure.messaging.eventgrid.EventGridPublisherClient;
import com.azure.messaging.eventgrid.EventGridPublisherClientBuilder;
import com.function.domain.model.DomainEvent;

/**
 * Implementación de EventGridPublisher que publica eventos en Azure Event Grid.
 */
public class EventGridPublisherImpl implements EventGridPublisher {

    private final EventGridPublisherClient<EventGridEvent> client;

    /**
     * Constructor que inicializa el cliente de Event Grid.
     * @param endpoint Endpoint del topic de Event Grid
     * @param key Clave de acceso al topic
     */
    public EventGridPublisherImpl(String endpoint, String key) {
        this.client = new EventGridPublisherClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(key))
                .buildEventGridEventPublisherClient();
    }

    /**
     * Publica un evento de dominio en Azure Event Grid.
     * @param event Evento de dominio a publicar
     */
    @Override
    public void publish(DomainEvent event) {
        // Construcción y envío del evento a Event Grid
        EventGridEvent gridEvent = new EventGridEvent(
            event.getType(), // subject y tipo de operación CRUD
            event.getType(), // eventType explícito
            BinaryData.fromObject(event.getData()),
            "1.0"
        );
        client.sendEvent(gridEvent);
    }
} 