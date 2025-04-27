package com.function.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.function.application.usecase.CreateUserUseCase;
import com.function.application.usecase.PublishDomainEventUseCase;
import com.function.domain.model.User;
import com.function.infraestructure.repository.UserRepository;
import com.function.infraestructure.db.UserRepositoryImpl;
import com.infrastructure.eventgrid.EventGridPublisherImpl;

import java.util.Optional;

public class CreateUserFunction {

    @FunctionName("CreateUser")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS, route = "users") HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Ejecutando función: CreateUser");

        try {
            String requestBody = request.getBody().orElse("");
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(requestBody, User.class);

            UserRepository repository = new UserRepositoryImpl();
            String endpoint = System.getenv("EVENT_GRID_TOPIC_ENDPOINT");
            String key = System.getenv("EVENT_GRID_TOPIC_KEY");
            EventGridPublisherImpl publisher = new EventGridPublisherImpl(endpoint, key);
            CreateUserUseCase useCase = new CreateUserUseCase(
                repository,
                new PublishDomainEventUseCase(publisher)
            );

            boolean created = useCase.execute(user);

            if (created) {
                return request.createResponseBuilder(HttpStatus.CREATED)
                        .body("{\"mensaje\":\"Usuario creado exitosamente\"}")
                        .header("Content-Type", "application/json")
                        .build();
            } else {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                        .body("{\"mensaje\":\"Error al crear el usuario\"}")
                        .header("Content-Type", "application/json")
                        .build();
            }
        } catch (Exception e) {
            context.getLogger().severe("Error al crear usuario: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"mensaje\":\"Error interno del servidor\"}")
                    .header("Content-Type", "application/json")
                    .build();
        }
    }
}