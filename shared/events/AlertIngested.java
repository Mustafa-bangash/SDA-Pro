package com.sdapro.shared.events;

import java.time.Instant;
import java.util.UUID;

// PATTERN: Observer
// This is the domain event published by alert-ingestion-service
// and consumed by enrichment-correlation-service, audit-service, soc-dashboard
public class AlertIngested {

    private final String eventType = "AlertIngested";
    private final UUID eventId;
    private final Instant occurredAt;

    // Alert data
    private final UUID alertId;
    private final String sourceType;       // SPLUNK, CROWDSTRIKE, FIREWALL
    private final String severity;         // LOW, MEDIUM, HIGH, CRITICAL
    private final String sourceIp;
    private final String destinationIp;
    private final String normalizedSummary;
    private final boolean isComposite;     // true if AlertCampaign or IncidentCluster

    public AlertIngested(UUID alertId, String sourceType, String severity,
                         String sourceIp, String destinationIp,
                         String normalizedSummary, boolean isComposite) {
        this.eventId = UUID.randomUUID();
        this.occurredAt = Instant.now();
        this.alertId = alertId;
        this.sourceType = sourceType;
        this.severity = severity;
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.normalizedSummary = normalizedSummary;
        this.isComposite = isComposite;
    }

    public String getEventType() { return eventType; }
    public UUID getEventId() { return eventId; }
    public Instant getOccurredAt() { return occurredAt; }
    public UUID getAlertId() { return alertId; }
    public String getSourceType() { return sourceType; }
    public String getSeverity() { return severity; }
    public String getSourceIp() { return sourceIp; }
    public String getDestinationIp() { return destinationIp; }
    public String getNormalizedSummary() { return normalizedSummary; }
    public boolean isComposite() { return isComposite; }

    @Override
    public String toString() {
        return "AlertIngested{eventId=" + eventId +
               ", alertId=" + alertId +
               ", source=" + sourceType +
               ", severity=" + severity + "}";
    }
}
