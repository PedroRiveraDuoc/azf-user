package com.infrastructure.eventgrid;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.azure.messaging.eventgrid.EventGridEvent;
import com.azure.messaging.eventgrid.EventGridPublisherClient;
import com.azure.messaging.eventgrid.EventGridPublisherClientBuilder;
import com.function.domain.model.DomainEvent;

public class EventGridPublisherImpl implements EventGridPublisher {

    private final EventGridPublisherClient<EventGridEvent> client;

    public EventGridPublisherImpl(String endpoint, String key) {
        this.client = new EventGridPublisherClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(key))
                .buildEventGridEventPublisherClient();
    }

    @Override
    public void publish(DomainEvent event) {
        EventGridEvent gridEvent = new EventGridEvent(
            event.getType(),
            "com.function.domain.model.DomainEvent",
            BinaryData.fromObject(event.getData()),
            "1.0"
        );
        client.sendEvent(gridEvent);
    }
} 