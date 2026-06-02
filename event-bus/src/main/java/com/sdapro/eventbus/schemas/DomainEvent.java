package com.sdapro.eventbus.schemas;

import java.time.Instant;
import java.util.UUID;

// PATTERN: Observer
// Base schema for all domain events published to the event bus.
// Every event has a unique ID, type, timestamp, and source service.
public class DomainEvent {

    private final String eventId;
    private final String eventType;
    private final String sourceService;
    private final String occurredAt;
    private String payload;

    public DomainEvent(String eventType, String sourceService, String payload) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.sourceService = sourceService;
        this.occurredAt = Instant.now().toString();
        this.payload = payload;
    }

    public String getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public String getSourceService() { return sourceService; }
    public String getOccurredAt() { return occurredAt; }
    public String getPayload() { return payload; }

    @Override
    public String toString() {
        return "DomainEvent{eventId=" + eventId
                + ", type=" + eventType
                + ", source=" + sourceService
                + ", at=" + occurredAt + "}";
    }
}
