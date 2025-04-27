package com.function.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.function.application.usecase.DeleteUserUseCase;
import com.function.application.usecase.PublishDomainEventUseCase;
import com.function.infraestructure.repository.UserRepository;
import com.function.infraestructure.db.UserRepositoryImpl;
import com.infrastructure.eventgrid.EventGridPublisherImpl;

import java.util.Optional;

public class DeleteUserFunction {

    @FunctionName("DeleteUser")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.DELETE }, authLevel = AuthorizationLevel.ANONYMOUS, route = "users/{id}") HttpRequestMessage<Optional<String>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {

        context.getLogger().info("Ejecutando función: DeleteUser");

        try {
            int userId = Integer.parseInt(id);
            UserRepository repository = new UserRepositoryImpl();
            String endpoint = System.getenv("EVENT_GRID_TOPIC_ENDPOINT");
            String key = System.getenv("EVENT_GRID_TOPIC_KEY");
            EventGridPublisherImpl publisher = new EventGridPublisherImpl(endpoint, key);
            DeleteUserUseCase useCase = new DeleteUserUseCase(
                repository,
                new PublishDomainEventUseCase(publisher)
            );

            boolean deleted = useCase.execute(userId);

            if (deleted) {
                return request.createResponseBuilder(HttpStatus.OK)
                        .body("{\"mensaje\":\"Usuario eliminado exitosamente\"}")
                        .header("Content-Type", "application/json")
                        .build();
            } else {
                return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                        .body("{\"mensaje\":\"Usuario no encontrado\"}")
                        .header("Content-Type", "application/json")
                        .build();
            }
        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("{\"mensaje\":\"ID inválido\"}")
                    .header("Content-Type", "application/json")
                    .build();
        } catch (Exception e) {
            context.getLogger().severe("Error al eliminar usuario: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"mensaje\":\"Error interno del servidor\"}")
                    .header("Content-Type", "application/json")
                    .build();
        }
    }
}