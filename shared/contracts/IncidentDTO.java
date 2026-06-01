package com.sdapro.shared.contracts;

import java.time.Instant;
import java.util.UUID;

// Shared DTO for incident data passed between services
// incident-management-service produces; soc-dashboard, response-orchestration consume
public class IncidentDTO {

    private UUID incidentId;
    private String currentState;     // NEW, UNDER_TRIAGE, CONTAINMENT, etc.
    private String severity;
    private UUID triggeringAlertId;
    private String assignedAnalyst;
    private Instant createdAt;
    private Instant lastUpdatedAt;
    private String stateHistory;     // comma-separated state transition log

    public IncidentDTO() {}

    public IncidentDTO(UUID incidentId, String currentState, String severity,
                       UUID triggeringAlertId, String assignedAnalyst,
                       Instant createdAt, Instant lastUpdatedAt,
                       String stateHistory) {
        this.incidentId = incidentId;
        this.currentState = currentState;
        this.severity = severity;
        this.triggeringAlertId = triggeringAlertId;
        this.assignedAnalyst = assignedAnalyst;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
        this.stateHistory = stateHistory;
    }

    public UUID getIncidentId() { return incidentId; }
    public void setIncidentId(UUID incidentId) { this.incidentId = incidentId; }
    public String getCurrentState() { return currentState; }
    public void setCurrentState(String currentState) { this.currentState = currentState; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public UUID getTriggeringAlertId() { return triggeringAlertId; }
    public void setTriggeringAlertId(UUID triggeringAlertId) { this.triggeringAlertId = triggeringAlertId; }
    public String getAssignedAnalyst() { return assignedAnalyst; }
    public void setAssignedAnalyst(String assignedAnalyst) { this.assignedAnalyst = assignedAnalyst; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getLastUpdatedAt() { return lastUpdatedAt; }
    public void setLastUpdatedAt(Instant lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }
    public String getStateHistory() { return stateHistory; }
    public void setStateHistory(String stateHistory) { this.stateHistory = stateHistory; }
}
