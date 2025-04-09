package com.function.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.function.application.usecase.GetUserUseCase;
import com.function.infraestructure.db.UserRepositoryImpl;
import com.function.util.mapper.UserMapper;

public class GetUserByIdFunction {

    @FunctionName("GetUserById")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS, route = "users/{id}") HttpRequestMessage<Void> request,
            @BindingName("id") String id,
            final ExecutionContext context) {

        context.getLogger().info("Ejecutando función: GetUserById");

        GetUserUseCase useCase = new GetUserUseCase(new UserRepositoryImpl());

        try {
            int userId = Integer.parseInt(id);
            var usuarioOpt = useCase.getById(userId);
            if (usuarioOpt.isPresent()) {
                return request.createResponseBuilder(HttpStatus.OK)
                        .body(UserMapper.toJson(usuarioOpt.get()))
                        .header("Content-Type", "application/json")
                        .build();
            } else {
                return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                        .body("{\"error\":\"Usuario no encontrado\"}")
                        .header("Content-Type", "application/json")
                        .build();
            }
        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"ID inválido\"}")
                    .header("Content-Type", "application/json")
                    .build();
        } catch (Exception e) {
            context.getLogger().severe("Error al obtener usuario: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener usuario\"}")
                    .header("Content-Type", "application/json")
                    .build();
        }
    }
}