package com.function.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.function.application.usecase.CreateUserUseCase;
import com.function.domain.model.User;
import com.function.infraestructure.db.UserRepositoryImpl;
import com.function.util.mapper.UserMapper;

import java.util.Optional;

public class CreateUserFunction {

    @FunctionName("CreateUser")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
                    
            final ExecutionContext context) {

        context.getLogger().info("Ejecutando función: CreateUser");

        try {
            String body = request.getBody().orElse("");
            context.getLogger().info("[CreateUser] Body recibido: " + body);
            User user = UserMapper.fromJson(body);

            CreateUserUseCase useCase = new CreateUserUseCase(new UserRepositoryImpl());
            boolean created = useCase.execute(user);

            context.getLogger().info("[CreateUser] Resultado creación: " + created);

            if (created) {
                return request.createResponseBuilder(HttpStatus.OK)
                        .body("{\"mensaje\":\"Usuario creado exitosamente\"}")
                        .header("Content-Type", "application/json")
                        .build();
            } else {
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\":\"No se pudo crear el usuario\"}")
                        .header("Content-Type", "application/json")
                        .build();
            }

        } catch (Exception e) {
            context.getLogger().severe("[CreateUser] Error: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Entrada inválida: " + e.getMessage() + "\"}")
                    .header("Content-Type", "application/json")
                    .build();
        }
    }
}