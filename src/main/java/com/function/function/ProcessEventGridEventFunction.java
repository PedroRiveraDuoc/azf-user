package com.function.function;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.google.gson.JsonElement;
import com.domain.model.event.EventAudit;
import com.domain.repository.EventAuditRepository;
import com.infrastructure.repository.EventAuditRepositoryImpl;
import java.time.OffsetDateTime;
import java.util.logging.Logger;
import javax.sql.DataSource;
import oracle.jdbc.pool.OracleDataSource;

/**
 * Función Azure que procesa eventos de Event Grid y los persiste en auditoría Oracle.
 */
public class ProcessEventGridEventFunction {
    private final EventAuditRepository eventAuditRepository;

    /**
     * Constructor requerido por Azure Functions. Inicializa el repositorio de auditoría.
     */
    public ProcessEventGridEventFunction() {
        DataSource dataSource = crearDataSourceOracle();
        this.eventAuditRepository = new EventAuditRepositoryImpl(dataSource);
    }

    /**
     * Constructor para inyección manual (tests, etc).
     * @param eventAuditRepository Repositorio de auditoría a usar
     */
    public ProcessEventGridEventFunction(EventAuditRepository eventAuditRepository) {
        this.eventAuditRepository = eventAuditRepository;
    }

    /**
     * Procesa un evento recibido desde Event Grid y lo persiste en la base de datos de auditoría.
     * @param content Contenido del evento recibido
     * @param context Contexto de ejecución de Azure Functions
     */
    @FunctionName("ProcessEventGridEvent")
    public void run(
        @EventGridTrigger(name = "eventGridEvent") String content,
        final ExecutionContext context) {

        Logger logger = context.getLogger();
        logger.info("[ProcessEventGridEvent] Inicio de ejecución");

        try {
            Gson gson = new Gson();
            JsonObject eventGridEvent = gson.fromJson(content, JsonObject.class);

            String eventId = eventGridEvent.get("id").getAsString();
            String eventType = eventGridEvent.get("eventType").getAsString();
            OffsetDateTime eventTime = OffsetDateTime.parse(eventGridEvent.get("eventTime").getAsString());
            
            logger.info("Procesando evento - ID: " + eventId + ", Tipo: " + eventType);

            JsonElement dataElement = eventGridEvent.get("data");
            String data;
            
            if (dataElement.isJsonObject()) {
                data = dataElement.getAsJsonObject().toString();
                logger.info("Data recibida como objeto JSON");
            } else {
                data = dataElement.getAsString();
                logger.info("Data recibida como string");
            }

            // Persistencia de evento de auditoría
            try {
                logger.info("Iniciando persistencia del evento en auditoría - ID: " + eventId);
                
                EventAudit eventAudit = new EventAudit(
                    eventId,
                    eventType,
                    eventTime,
                    data
                );
                
                eventAuditRepository.save(eventAudit);
                logger.info("Evento de auditoría guardado exitosamente en la base de datos - ID: " + eventId);
                
            } catch (Exception e) {
                logger.severe("Error crítico al persistir evento de auditoría. ID: " + eventId + ". Mensaje: " + e.getMessage());
                java.io.StringWriter sw = new java.io.StringWriter();
                java.io.PrintWriter pw = new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.severe("Stack trace completo: " + sw.toString());
            }

            logger.info("Tipo de evento procesado: " + eventType);
            logger.info("[ProcessEventGridEvent] Fin de ejecución exitosa. ID: " + eventId);

        } catch (Exception e) {
            logger.severe("[ProcessEventGridEvent] Error crítico en la función: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.severe("Stack trace completo: " + sw.toString());
            throw e;
        }
    }

    /**
     * Crea y configura el DataSource Oracle usando variables de entorno.
     * @return DataSource configurado
     */
    private DataSource crearDataSourceOracle() {
        try {
            String user = System.getenv("ORACLE_USER");
            String password = System.getenv("ORACLE_PASSWORD");
            String tnsName = System.getenv("ORACLE_TNS_NAME");
            String walletPath = System.getenv("ORACLE_WALLET_PATH");
            String url = "jdbc:oracle:thin:@" + tnsName + "?TNS_ADMIN=" + walletPath;
            OracleDataSource ds = new OracleDataSource();
            ds.setURL(url);
            ds.setUser(user);
            ds.setPassword(password);
            return ds;
        } catch (Exception e) {
            throw new RuntimeException("Error creando DataSource Oracle", e);
        }
    }
}