package com.domain.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public class DomainEvent {
    private final String id;
    private final String eventType;
    private final OffsetDateTime eventTime;
    private final Object data;

    public DomainEvent(String id, String eventType, OffsetDateTime eventTime, Object data) {
        this.id = id;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public OffsetDateTime getEventTime() {
        return eventTime;
    }

    public Object getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainEvent that = (DomainEvent) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(eventType, that.eventType) &&
               Objects.equals(eventTime, that.eventTime) &&
               Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventType, eventTime, data);
    }
} 