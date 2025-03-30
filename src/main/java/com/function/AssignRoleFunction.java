package com.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Función serverless para asignar un rol a un usuario en Oracle.
 * Espera parámetros 'userId' y 'rolId' por query o cuerpo ("userId,rolId").
 */
public class AssignRoleFunction {

    @FunctionName("AssignRole")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Procesando asignación de rol a usuario.");

        String userId = request.getQueryParameters().get("userId");
        String rolId = request.getQueryParameters().get("rolId");

        Optional<String> bodyOpt = request.getBody();
        if ((userId == null || rolId == null) && bodyOpt.isPresent()) {
            String[] parts = bodyOpt.get().trim().split(",");
            if (parts.length >= 2) {
                userId = (userId == null || userId.isEmpty()) ? parts[0].trim() : userId;
                rolId = (rolId == null || rolId.isEmpty()) ? parts[1].trim() : rolId;
            }
        }

        if (userId == null || rolId == null || userId.isEmpty() || rolId.isEmpty()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Se requieren los parámetros 'userId' y 'rolId'.\"}")
                    .header("Content-Type", "application/json")
                    .build();
        }

        try {
            int userIdInt = Integer.parseInt(userId);
            int rolIdInt = Integer.parseInt(rolId);

            String sql = "INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (?, ?)";

            try (Connection conn = OracleDBConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, userIdInt);
                ps.setInt(2, rolIdInt);

                int filas = ps.executeUpdate();

                if (filas > 0) {
                    return request.createResponseBuilder(HttpStatus.OK)
                            .body("{\"mensaje\":\"Rol asignado exitosamente.\", \"userId\":\"" + userId
                                    + "\", \"rolId\":\"" + rolId + "\"}")
                            .header("Content-Type", "application/json")
                            .build();
                } else {
                    return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("{\"error\":\"No se pudo asignar el rol.\"}")
                            .header("Content-Type", "application/json")
                            .build();
                }

            }

        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"'userId' y 'rolId' deben ser numéricos.\"}")
                    .header("Content-Type", "application/json")
                    .build();

        } catch (SQLException e) {
            context.getLogger().severe("Error SQL: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error SQL al asignar rol: " + e.getMessage() + "\"}")
                    .header("Content-Type", "application/json")
                    .build();
        }
    }
}