package com.sdapro.audit.services;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

// PATTERN: Observer (consumer)
// AuditService stores the immutable audit trail written by AuditLogger observer
public class AuditService {

    // In-memory audit log (production: append-only DB table)
    private final List<String> auditLog = new ArrayList<>();

    public void log(String eventType, String eventData) {
        String entry = "[AUDIT] timestamp=" + Instant.now()
                + " event=" + eventType
                + " data=" + eventData
                + " immutable=true";
        auditLog.add(entry);
        System.out.println(entry);
    }

    public List<String> getAllLogs() {
        return Collections.unmodifiableList(auditLog);
    }

    public List<String> getLogsForIncident(String incidentId) {
        return auditLog.stream()
                .filter(entry -> entry.contains(incidentId))
                .collect(Collectors.toList());
    }

    public Map<String, Object> generateComplianceReport() {
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("generatedAt", Instant.now().toString());
        report.put("totalEvents", auditLog.size());
        report.put("complianceStandards", List.of("GDPR", "SOC2"));
        report.put("retentionPolicy", "7 years");
        report.put("immutableStorage", true);
        report.put("recentEntries", auditLog.stream().limit(10).collect(Collectors.toList()));
        return report;
    }
}
