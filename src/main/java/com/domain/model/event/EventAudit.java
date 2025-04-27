package com.domain.model.event;

import java.time.OffsetDateTime;

public class EventAudit {
    private String idEvento;
    private String tipoEvento;
    private OffsetDateTime fechaEvento;
    private String datos;

    public EventAudit(String idEvento, String tipoEvento, OffsetDateTime fechaEvento, String datos) {
        this.idEvento = idEvento;
        this.tipoEvento = tipoEvento;
        this.fechaEvento = fechaEvento;
        this.datos = datos;
    }

    // Getters
    public String getIdEvento() {
        return idEvento;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public OffsetDateTime getFechaEvento() {
        return fechaEvento;
    }

    public String getDatos() {
        return datos;
    }
} 