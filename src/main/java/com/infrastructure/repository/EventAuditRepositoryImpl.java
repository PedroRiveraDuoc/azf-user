package com.infrastructure.repository;

import com.domain.model.event.EventAudit;
import com.domain.repository.EventAuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import javax.sql.DataSource;

public class EventAuditRepositoryImpl implements EventAuditRepository {
    private static final Logger logger = LoggerFactory.getLogger(EventAuditRepositoryImpl.class);
    private static final String INSERT_SQL = "INSERT INTO eventos_auditoria (id_evento, tipo_evento, fecha_evento, datos) VALUES (?, ?, ?, ?)";
    
    private final DataSource dataSource;

    public EventAuditRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(EventAudit eventAudit) throws RuntimeException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
            
            logger.info("Guardando evento de auditoría con ID: {}", eventAudit.getIdEvento());
            
            pstmt.setString(1, eventAudit.getIdEvento());
            pstmt.setString(2, eventAudit.getTipoEvento());
            pstmt.setObject(3, java.sql.Timestamp.from(eventAudit.getFechaEvento().toInstant()));
            pstmt.setString(4, eventAudit.getDatos());
            
            pstmt.executeUpdate();
            logger.info("Evento guardado exitosamente con ID: {}", eventAudit.getIdEvento());
            
        } catch (SQLException e) {
            logger.error("Error al guardar el evento de auditoría: {}", e.getMessage());
            throw new RuntimeException("Error al persistir el evento de auditoría", e);
        }
    }
} 