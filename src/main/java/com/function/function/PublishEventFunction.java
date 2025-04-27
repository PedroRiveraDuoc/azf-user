package com.function.function;

import com.function.application.usecase.PublishDomainEventUseCase;
import com.function.domain.model.DomainEvent;
import com.infrastructure.eventgrid.EventGridPublisherImpl;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

import java.util.UUID;

public class PublishEventFunction {

    @FunctionName("PublishEvent")
    public HttpResponseMessage run(
        @HttpTrigger(name = "req",
                     methods = {HttpMethod.POST},
                     authLevel = AuthorizationLevel.FUNCTION)
                     HttpRequestMessage<String> request,
        final ExecutionContext context) {

        context.getLogger().info("[PublishEvent] Petición recibida");
        String endpoint = System.getenv("EVENT_GRID_TOPIC_ENDPOINT");
        String key = System.getenv("EVENT_GRID_TOPIC_KEY");
        context.getLogger().info("[PublishEvent] Endpoint: " + endpoint);
        context.getLogger().info("[PublishEvent] Key: " + (key != null ? "[OK]" : "[NO ENCONTRADA]"));

        var publisher = new EventGridPublisherImpl(endpoint, key);
        var useCase = new PublishDomainEventUseCase(publisher);

        String body = request.getBody();
        context.getLogger().info("[PublishEvent] Body recibido: " + body);
        DomainEvent ev = new DomainEvent(
                "duoc.user.customEvent",
                body
        );

        try {
            useCase.execute(ev);
            context.getLogger().info("[PublishEvent] Evento publicado correctamente");
            return request.createResponseBuilder(HttpStatus.OK)
                          .body("Evento publicado")
                          .build();
        } catch (Exception e) {
            context.getLogger().severe("[PublishEvent] Error al publicar evento: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                          .body("Error al publicar evento: " + e.getMessage())
                          .build();
        }
    }
} 