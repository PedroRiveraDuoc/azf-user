package com.infrastructure.repository;

import com.domain.model.event.EventAudit;
import com.domain.repository.EventAuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Implementación de EventAuditRepository para persistir eventos de auditoría en Oracle.
 */
public class EventAuditRepositoryImpl implements EventAuditRepository {
    private static final Logger logger = LoggerFactory.getLogger(EventAuditRepositoryImpl.class);
    private static final String INSERT_SQL = "INSERT INTO eventos_auditoria (id_evento, tipo_evento, fecha_evento, datos) VALUES (?, ?, ?, ?)";
    
    private final DataSource dataSource;

    /**
     * Constructor que recibe el DataSource para la conexión a la base de datos.
     * @param dataSource DataSource configurado para Oracle
     */
    public EventAuditRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Guarda un evento de auditoría en la base de datos.
     * @param eventAudit Evento de auditoría a guardar
     * @throws RuntimeException Si ocurre un error durante la persistencia
     */
    @Override
    public void save(EventAudit eventAudit) throws RuntimeException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
            // Acción clave: persistir evento de auditoría
            pstmt.setString(1, eventAudit.getIdEvento());
            pstmt.setString(2, eventAudit.getTipoEvento());
            pstmt.setObject(3, java.sql.Timestamp.from(eventAudit.getFechaEvento().toInstant()));
            pstmt.setString(4, eventAudit.getDatos());
            pstmt.executeUpdate();
            logger.info("Evento de auditoría guardado exitosamente en la base de datos. ID: {}", eventAudit.getIdEvento());
        } catch (SQLException e) {
            logger.error("Error crítico al guardar el evento de auditoría. ID: {}. Mensaje: {}", eventAudit.getIdEvento(), e.getMessage());
            throw new RuntimeException("Error al persistir el evento de auditoría", e);
        }
    }
} 