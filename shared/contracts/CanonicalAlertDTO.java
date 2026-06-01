package com.sdapro.shared.contracts;

import java.time.Instant;
import java.util.UUID;

// Shared DTO used across services as the canonical alert contract
// alert-ingestion-service produces this; enrichment-correlation-service consumes it
public class CanonicalAlertDTO {

    private UUID alertId;
    private String sourceType;        // SPLUNK, CROWDSTRIKE, FIREWALL
    private String severity;          // LOW, MEDIUM, HIGH, CRITICAL
    private String sourceIp;
    private String destinationIp;
    private String eventType;         // LOGIN_FAILURE, MALWARE, PORT_SCAN, etc.
    private String summary;
    private Instant detectedAt;
    private String rawPayload;        // Original raw format stored as string

    public CanonicalAlertDTO() {}

    public CanonicalAlertDTO(UUID alertId, String sourceType, String severity,
                              String sourceIp, String destinationIp,
                              String eventType, String summary,
                              Instant detectedAt, String rawPayload) {
        this.alertId = alertId;
        this.sourceType = sourceType;
        this.severity = severity;
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.eventType = eventType;
        this.summary = summary;
        this.detectedAt = detectedAt;
        this.rawPayload = rawPayload;
    }

    public UUID getAlertId() { return alertId; }
    public void setAlertId(UUID alertId) { this.alertId = alertId; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getSourceIp() { return sourceIp; }
    public void setSourceIp(String sourceIp) { this.sourceIp = sourceIp; }
    public String getDestinationIp() { return destinationIp; }
    public void setDestinationIp(String destinationIp) { this.destinationIp = destinationIp; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public Instant getDetectedAt() { return detectedAt; }
    public void setDetectedAt(Instant detectedAt) { this.detectedAt = detectedAt; }
    public String getRawPayload() { return rawPayload; }
    public void setRawPayload(String rawPayload) { this.rawPayload = rawPayload; }
}
