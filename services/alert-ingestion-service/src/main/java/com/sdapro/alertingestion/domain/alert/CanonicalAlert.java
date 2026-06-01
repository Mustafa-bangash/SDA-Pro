package com.sdapro.alertingestion.domain.alert;

import java.time.Instant;
import java.util.UUID;

// PATTERN: Factory Method (Product)
// RATIONALE: All normalizers produce this unified canonical format
// regardless of the original source format (Splunk, CrowdStrike, Firewall)
public class CanonicalAlert {

    private UUID id;
    private String sourceType;
    private String sourceIp;
    private String destinationIp;
    private Severity severity;
    private String eventType;
    private String description;
    private Instant timestamp;
    private String rawPayload;

    public CanonicalAlert() {
        this.id = UUID.randomUUID();
        this.timestamp = Instant.now();
    }

    public UUID getId() { return id; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public String getSourceIp() { return sourceIp; }
    public void setSourceIp(String sourceIp) { this.sourceIp = sourceIp; }
    public String getDestinationIp() { return destinationIp; }
    public void setDestinationIp(String destinationIp) { this.destinationIp = destinationIp; }
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Instant getTimestamp() { return timestamp; }
    public String getRawPayload() { return rawPayload; }
    public void setRawPayload(String rawPayload) { this.rawPayload = rawPayload; }

    @Override
    public String toString() {
        return "CanonicalAlert{id=" + id + ", sourceType=" + sourceType +
               ", severity=" + severity + ", sourceIp=" + sourceIp + "}";
    }
}
