package com.sdapro.shared.events;

import java.time.Instant;
import java.util.UUID;

// PATTERN: Observer
// Published by incident-management-service when a new incident is created
// Consumed by: soc-dashboard, notification-service, response-orchestration-service, audit-service
public class IncidentCreated {

    private final String eventType = "IncidentCreated";
    private final UUID eventId;
    private final Instant occurredAt;

    // Incident data
    private final UUID incidentId;
    private final UUID triggeringAlertId;
    private final String severity;          // LOW, MEDIUM, HIGH, CRITICAL
    private final String initialState;      // Always "NEW"
    private final String correlationRule;   // Which rule triggered incident creation
    private final String assignedAnalyst;   // null if auto-assigned

    public IncidentCreated(UUID incidentId, UUID triggeringAlertId,
                           String severity, String correlationRule,
                           String assignedAnalyst) {
        this.eventId = UUID.randomUUID();
        this.occurredAt = Instant.now();
        this.incidentId = incidentId;
        this.triggeringAlertId = triggeringAlertId;
        this.severity = severity;
        this.initialState = "NEW";
        this.correlationRule = correlationRule;
        this.assignedAnalyst = assignedAnalyst;
    }

    public String getEventType() { return eventType; }
    public UUID getEventId() { return eventId; }
    public Instant getOccurredAt() { return occurredAt; }
    public UUID getIncidentId() { return incidentId; }
    public UUID getTriggeringAlertId() { return triggeringAlertId; }
    public String getSeverity() { return severity; }
    public String getInitialState() { return initialState; }
    public String getCorrelationRule() { return correlationRule; }
    public String getAssignedAnalyst() { return assignedAnalyst; }

    @Override
    public String toString() {
        return "IncidentCreated{eventId=" + eventId +
               ", incidentId=" + incidentId +
               ", severity=" + severity +
               ", state=" + initialState + "}";
    }
}
