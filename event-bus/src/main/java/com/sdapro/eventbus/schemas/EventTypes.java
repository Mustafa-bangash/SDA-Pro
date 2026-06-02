package com.sdapro.eventbus.schemas;

// PATTERN: Observer
// Canonical event type definitions for the SDA-Pro event bus.
// All publishers and subscribers must use these constants to ensure
// consistent event routing. This is the single source of truth for
// event type strings across all services.
public final class EventTypes {

    private EventTypes() {}

    // Alert lifecycle events
    public static final String ALERT_INGESTED         = "AlertIngested";
    public static final String ALERT_ENRICHED         = "AlertEnriched";

    // Incident lifecycle events
    public static final String INCIDENT_CREATED       = "IncidentCreated";
    public static final String INCIDENT_STATE_CHANGED = "IncidentStateChanged";

    // Response events
    public static final String RESPONSE_ACTION_EXECUTED = "ResponseActionExecuted";

    // Threat intel events
    public static final String THREAT_INTEL_UPDATED   = "ThreatIntelUpdated";

    // Subscribers per event type (documentation)
    // AlertIngested       → Enrichment, Dashboard, Audit
    // AlertEnriched       → Correlation, Dashboard, Audit
    // IncidentCreated     → Dashboard, Notification, ResponseOrchestration
    // IncidentStateChanged→ Dashboard, Audit, Metrics
    // ResponseActionExecuted → Dashboard, Audit, Notification
    // ThreatIntelUpdated  → Enrichment (cache invalidation)
}
