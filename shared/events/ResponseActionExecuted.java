package com.sdapro.shared.events;

import java.time.Instant;
import java.util.UUID;

// PATTERN: Observer
// Published by response-orchestration-service after executing a response action
// Consumed by: soc-dashboard, audit-service, notification-service
public class ResponseActionExecuted {

    private final String eventType = "ResponseActionExecuted";
    private final UUID eventId;
    private final Instant occurredAt;

    private final UUID incidentId;
    private final String actionType;     // BLOCK_IP, ISOLATE_ENDPOINT, DISABLE_USER, QUARANTINE_FILE
    private final String targetAsset;    // IP, hostname, username, file hash
    private final String outcome;        // SUCCESS, FAILED, ROLLED_BACK
    private final String executedBy;     // "AUTOMATED" or analyst ID
    private final boolean reversible;
    private final String errorMessage;   // null if outcome = SUCCESS

    public ResponseActionExecuted(UUID incidentId, String actionType,
                                  String targetAsset, String outcome,
                                  String executedBy, boolean reversible,
                                  String errorMessage) {
        this.eventId = UUID.randomUUID();
        this.occurredAt = Instant.now();
        this.incidentId = incidentId;
        this.actionType = actionType;
        this.targetAsset = targetAsset;
        this.outcome = outcome;
        this.executedBy = executedBy;
        this.reversible = reversible;
        this.errorMessage = errorMessage;
    }

    public String getEventType() { return eventType; }
    public UUID getEventId() { return eventId; }
    public Instant getOccurredAt() { return occurredAt; }
    public UUID getIncidentId() { return incidentId; }
    public String getActionType() { return actionType; }
    public String getTargetAsset() { return targetAsset; }
    public String getOutcome() { return outcome; }
    public String getExecutedBy() { return executedBy; }
    public boolean isReversible() { return reversible; }
    public String getErrorMessage() { return errorMessage; }

    @Override
    public String toString() {
        return "ResponseActionExecuted{incidentId=" + incidentId +
               ", action=" + actionType +
               ", target=" + targetAsset +
               ", outcome=" + outcome + "}";
    }
}
