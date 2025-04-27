package com.domain.repository;

import com.domain.model.event.EventAudit;

public interface EventAuditRepository {
    /**
     * Guarda un evento de auditoría en la base de datos.
     * @param eventAudit El evento a guardar
     * @throws RuntimeException Si ocurre algún error durante la persistencia
     */
    void save(EventAudit eventAudit) throws RuntimeException;
} 