package com.sdapro.eventbus.subscribers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// PATTERN: Observer — Concrete Observer
// RATIONALE: Every domain event must be immutably logged for compliance
// (GDPR, SOC2). AuditLogger subscribes to all events and writes an
// immutable audit trail. It never needs to be called directly by any
// service — the event bus delivers events automatically.
public class AuditLogger implements EventObserver {

    private final List<String> auditTrail = new ArrayList<>();

    @Override
    public void onEvent(String eventType, String eventPayload) {
        String entry = buildAuditEntry(eventType, eventPayload);
        auditTrail.add(entry);
        System.out.println("[AuditLogger] " + entry);
    }

    private String buildAuditEntry(String eventType, String eventPayload) {
        return "[AUDIT] timestamp=" + Instant.now()
                + " event=" + eventType
                + " payload=" + eventPayload
                + " immutable=true";
    }

    @Override
    public String getObserverId() {
        return "AuditLogger";
    }

    public List<String> getAuditTrail() {
        return new ArrayList<>(auditTrail);
    }

    public int getEntryCount() {
        return auditTrail.size();
    }
}
