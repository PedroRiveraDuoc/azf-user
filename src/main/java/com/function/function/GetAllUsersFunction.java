package com.function.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.function.application.usecase.GetUserUseCase;
import com.function.infraestructure.db.UserRepositoryImpl;
import com.function.util.mapper.UserMapper;

public class GetAllUsersFunction {

    @FunctionName("GetAllUsers")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS, route = "users") HttpRequestMessage<Void> request,
            final ExecutionContext context) {

        context.getLogger().info("Ejecutando función: GetAllUsers");

        GetUserUseCase useCase = new GetUserUseCase(new UserRepositoryImpl());

        try {
            var usuarios = useCase.getAll();
            return request.createResponseBuilder(HttpStatus.OK)
                    .body(UserMapper.toJson(usuarios))
                    .header("Content-Type", "application/json")
                    .build();
        } catch (Exception e) {
            context.getLogger().severe("Error al obtener usuarios: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener usuarios\"}")
                    .header("Content-Type", "application/json")
                    .build();
        }
    }
}