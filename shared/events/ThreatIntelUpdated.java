package com.sdapro.shared.events;

import java.time.Instant;
import java.util.UUID;

// PATTERN: Observer
// Published by threat-intel-service when new threat intel arrives
// Consumed by: enrichment-correlation-service (triggers cache invalidation)
public class ThreatIntelUpdated {

    private final String eventType = "ThreatIntelUpdated";
    private final UUID eventId;
    private final Instant occurredAt;

    private final String indicatorType;   // IP_ADDRESS, DOMAIN, FILE_HASH
    private final String indicatorValue;  // The actual IP / domain / hash
    private final String newVerdict;      // MALICIOUS, SUSPICIOUS, CLEAN
    private final String source;          // VIRUSTOTAL, MISP, CUSTOM_FEED
    private final int confidenceScore;    // 0-100

    public ThreatIntelUpdated(String indicatorType, String indicatorValue,
                               String newVerdict, String source,
                               int confidenceScore) {
        this.eventId = UUID.randomUUID();
        this.occurredAt = Instant.now();
        this.indicatorType = indicatorType;
        this.indicatorValue = indicatorValue;
        this.newVerdict = newVerdict;
        this.source = source;
        this.confidenceScore = confidenceScore;
    }

    public String getEventType() { return eventType; }
    public UUID getEventId() { return eventId; }
    public Instant getOccurredAt() { return occurredAt; }
    public String getIndicatorType() { return indicatorType; }
    public String getIndicatorValue() { return indicatorValue; }
    public String getNewVerdict() { return newVerdict; }
    public String getSource() { return source; }
    public int getConfidenceScore() { return confidenceScore; }

    @Override
    public String toString() {
        return "ThreatIntelUpdated{indicator=" + indicatorValue +
               ", verdict=" + newVerdict +
               ", source=" + source + "}";
    }
}
