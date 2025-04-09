package com.function;

import com.function.infraestructure.db.OracleDBConnection;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.util.Optional;

/**
 * Azure Function que prueba conexión a Oracle utilizando Wallet.
 */
public class TestOracleConnection {

    @FunctionName("testOracleConnection")
    public HttpResponseMessage run(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.GET},
            authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {

        context.getLogger().info("Ejecutando función para probar conexión a Oracle.");

        boolean conectado = OracleDBConnection.testConnection();

        String mensaje = conectado
            ? "✅ Conexión exitosa a Oracle con Wallet"
            : "❌ Falló la conexión a Oracle";

        return request.createResponseBuilder(HttpStatus.OK)
            .body(mensaje)
            .build();
    }
}