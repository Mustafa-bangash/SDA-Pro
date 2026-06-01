package com.sdapro.alertingestion.domain.alert;

import java.time.Instant;
import java.util.UUID;

// PATTERN: Composite
// RATIONALE: Shared base for SingleAlert, AlertCampaign, IncidentCluster
public abstract class AbstractAlert implements AlertComponent {

    protected UUID id;
    protected Severity severity;
    protected Instant timestamp;
    protected String sourceType;

    public AbstractAlert(Severity severity, String sourceType) {
        this.id = UUID.randomUUID();
        this.severity = severity;
        this.timestamp = Instant.now();
        this.sourceType = sourceType;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    public String getSourceType() {
        return sourceType;
    }
}
