package com.function.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class DomainEvent {
    private final UUID id;
    private final String type;
    private final Object data;
    private final OffsetDateTime occurredOn;

    public DomainEvent(String type, Object data) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.data = data;
        this.occurredOn = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public OffsetDateTime getOccurredOn() {
        return occurredOn;
    }
} 