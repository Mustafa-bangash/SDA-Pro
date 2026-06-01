package com.sdapro.shared.events;

import java.time.Instant;
import java.util.UUID;

// PATTERN: Observer
// Published by enrichment-correlation-service after pipeline completes
// Consumed by: correlation engine, soc-dashboard, audit-service
public class AlertEnriched {

    private final String eventType = "AlertEnriched";
    private final UUID eventId;
    private final Instant occurredAt;

    private final UUID alertId;
    private final String geoLocation;
    private final String threatIntelScore;    // e.g. "MALICIOUS - Score: 95/100"
    private final String assetCriticality;   // e.g. "CRITICAL - Database Server"
    private final String classification;     // e.g. "CRITICAL - Immediate Response"
    private final boolean isDuplicate;
    private final String correlationAction;  // CREATE_INCIDENT, MONITOR_ONLY, ESCALATE

    public AlertEnriched(UUID alertId, String geoLocation, String threatIntelScore,
                         String assetCriticality, String classification,
                         boolean isDuplicate, String correlationAction) {
        this.eventId = UUID.randomUUID();
        this.occurredAt = Instant.now();
        this.alertId = alertId;
        this.geoLocation = geoLocation;
        this.threatIntelScore = threatIntelScore;
        this.assetCriticality = assetCriticality;
        this.classification = classification;
        this.isDuplicate = isDuplicate;
        this.correlationAction = correlationAction;
    }

    public String getEventType() { return eventType; }
    public UUID getEventId() { return eventId; }
    public Instant getOccurredAt() { return occurredAt; }
    public UUID getAlertId() { return alertId; }
    public String getGeoLocation() { return geoLocation; }
    public String getThreatIntelScore() { return threatIntelScore; }
    public String getAssetCriticality() { return assetCriticality; }
    public String getClassification() { return classification; }
    public boolean isDuplicate() { return isDuplicate; }
    public String getCorrelationAction() { return correlationAction; }

    @Override
    public String toString() {
        return "AlertEnriched{alertId=" + alertId +
               ", classification=" + classification +
               ", action=" + correlationAction + "}";
    }
}
