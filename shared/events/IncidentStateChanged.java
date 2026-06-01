package com.sdapro.shared.events;

import java.time.Instant;
import java.util.UUID;

// PATTERN: Observer
// Published by incident-management-service on every state transition
// Consumed by: soc-dashboard, audit-service, metrics-collector
public class IncidentStateChanged {

    private final String eventType = "IncidentStateChanged";
    private final UUID eventId;
    private final Instant occurredAt;

    private final UUID incidentId;
    private final String previousState;
    private final String newState;
    private final String triggeredBy;    // "ANALYST" or "AUTOMATED"
    private final String analystId;      // null if automated
    private final String reason;

    public IncidentStateChanged(UUID incidentId, String previousState,
                                String newState, String triggeredBy,
                                String analystId, String reason) {
        this.eventId = UUID.randomUUID();
        this.occurredAt = Instant.now();
        this.incidentId = incidentId;
        this.previousState = previousState;
        this.newState = newState;
        this.triggeredBy = triggeredBy;
        this.analystId = analystId;
        this.reason = reason;
    }

    public String getEventType() { return eventType; }
    public UUID getEventId() { return eventId; }
    public Instant getOccurredAt() { return occurredAt; }
    public UUID getIncidentId() { return incidentId; }
    public String getPreviousState() { return previousState; }
    public String getNewState() { return newState; }
    public String getTriggeredBy() { return triggeredBy; }
    public String getAnalystId() { return analystId; }
    public String getReason() { return reason; }

    @Override
    public String toString() {
        return "IncidentStateChanged{incidentId=" + incidentId +
               ", " + previousState + " -> " + newState +
               ", by=" + triggeredBy + "}";
    }
}
