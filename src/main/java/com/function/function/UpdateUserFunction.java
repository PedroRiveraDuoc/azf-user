package com.function.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.function.infraestructure.db.UserRepositoryImpl;
import com.function.util.mapper.UserMapper;
import com.function.application.usecase.UpdateUserUseCase;
import com.function.domain.model.User;

import java.util.Optional;

public class UpdateUserFunction {

    @FunctionName("UpdateUser")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.PUT }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Ejecutando función: UpdateUser");

        try {
            String body = request.getBody().orElse("");
            User user = UserMapper.fromJson(body);

            UpdateUserUseCase useCase = new UpdateUserUseCase(new UserRepositoryImpl());
            boolean updated = useCase.execute(user);

            if (updated) {
                return request.createResponseBuilder(HttpStatus.OK)
                        .body("{\"mensaje\":\"Usuario actualizado exitosamente\"}")
                        .header("Content-Type", "application/json")
                        .build();
            } else {
                return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                        .body("{\"error\":\"No se pudo actualizar el usuario\"}")
                        .header("Content-Type", "application/json")
                        .build();
            }

        } catch (Exception e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Entrada inválida: " + e.getMessage() + "\"}")
                    .header("Content-Type", "application/json")
                    .build();
        }
    }
}