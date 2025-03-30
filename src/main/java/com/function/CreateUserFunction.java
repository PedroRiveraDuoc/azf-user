package com.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Función serverless para crear un nuevo usuario en Oracle.
 * Espera un cuerpo con formato "nombre,email"
 */
public class CreateUserFunction {

    @FunctionName("CreateUser")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Solicitud recibida para crear un nuevo usuario.");

        String body = request.getBody().orElse("").trim();

        // Validar que el cuerpo no esté vacío
        if (body.isEmpty()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Se requiere un cuerpo en formato 'nombre,email'.\"}")
                    .header("Content-Type", "application/json")
                    .build();
        }

        // Separar los datos por coma
        String[] parts = body.split(",");
        if (parts.length < 2) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Formato inválido. Esperado: 'nombre,email'.\"}")
                    .header("Content-Type", "application/json")
                    .build();
        }

        // Validar que haya al menos nombre y email
        String nombre = parts[0].trim();
        String email = parts[1].trim();

        String sql = "INSERT INTO usuarios (nombre, email) VALUES (?, ?)";

        //Manejo de excepciones para la conexión a la base de datos
        try (Connection conn = OracleDBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, email);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                return request.createResponseBuilder(HttpStatus.OK)
                        .body("{\"mensaje\":\"Usuario creado exitosamente.\", \"nombre\":\"" + nombre
                                + "\", \"email\":\"" + email + "\"}")
                        .header("Content-Type", "application/json")
                        .build();
            } else {
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\":\"No se pudo crear el usuario.\"}")
                        .header("Content-Type", "application/json")
                        .build();
            }

        } catch (SQLException e) {
            context.getLogger().severe("Error SQL: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al insertar en la base de datos: " + e.getMessage() + "\"}")
                    .header("Content-Type", "application/json")
                    .build();
        }
    }
}