package com.function.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.function.application.usecase.DeleteUserUseCase;
import com.function.infraestructure.db.UserRepositoryImpl;

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
            DeleteUserUseCase useCase = new DeleteUserUseCase(new UserRepositoryImpl());

            boolean deleted = useCase.execute(userId);

            if (deleted) {
                return request.createResponseBuilder(HttpStatus.OK)
                        .body("{\"mensaje\":\"Usuario eliminado exitosamente\"}")
                        .header("Content-Type", "application/json")
                        .build();
            } else {
                return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                        .body("{\"error\":\"Usuario no encontrado o ya eliminado\"}")
                        .header("Content-Type", "application/json")
                        .build();
            }

        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"ID inválido\"}")
                    .header("Content-Type", "application/json")
                    .build();
        }
    }
}