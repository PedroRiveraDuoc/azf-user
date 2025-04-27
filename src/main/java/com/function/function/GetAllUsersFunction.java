package com.function.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.function.application.usecase.GetUserUseCase;
import com.function.application.usecase.PublishDomainEventUseCase;
import com.function.domain.model.User;
import com.function.infraestructure.repository.UserRepository;
import com.function.infraestructure.db.UserRepositoryImpl;
import com.infrastructure.eventgrid.EventGridPublisherImpl;

import java.util.List;
import java.util.Optional;

public class GetAllUsersFunction {

    @FunctionName("GetAllUsers")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS, route = "users") HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Ejecutando función: GetAllUsers");

        try {
            UserRepository repository = new UserRepositoryImpl();
            String endpoint = System.getenv("EVENT_GRID_TOPIC_ENDPOINT");
            String key = System.getenv("EVENT_GRID_TOPIC_KEY");
            EventGridPublisherImpl publisher = new EventGridPublisherImpl(endpoint, key);
            GetUserUseCase useCase = new GetUserUseCase(
                repository,
                new PublishDomainEventUseCase(publisher)
            );

            List<User> users = useCase.findAll();
            ObjectMapper mapper = new ObjectMapper();
            String jsonResponse = mapper.writeValueAsString(users);

            return request.createResponseBuilder(HttpStatus.OK)
                    .body(jsonResponse)
                    .header("Content-Type", "application/json")
                    .build();
        } catch (Exception e) {
            context.getLogger().severe("Error al obtener usuarios: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"mensaje\":\"Error interno del servidor\"}")
                    .header("Content-Type", "application/json")
                    .build();
        }
    }
}