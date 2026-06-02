package com.sdapro.audit.controllers;

import com.sdapro.audit.services.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

// PATTERN: Observer (consumer side)
// RATIONALE: AuditController exposes the immutable audit trail built by
// AuditLogger observer. Every event that flows through the event bus
// is automatically logged here without any service calling audit directly.
@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {

    private final AuditService auditService = new AuditService();

    @GetMapping("/logs")
    public ResponseEntity<List<String>> getAllLogs() {
        return ResponseEntity.ok(auditService.getAllLogs());
    }

    @GetMapping("/logs/incident/{incidentId}")
    public ResponseEntity<List<String>> getLogsForIncident(@PathVariable String incidentId) {
        return ResponseEntity.ok(auditService.getLogsForIncident(incidentId));
    }

    @GetMapping("/compliance/report")
    public ResponseEntity<Map<String, Object>> getComplianceReport() {
        return ResponseEntity.ok(auditService.generateComplianceReport());
    }

    @PostMapping("/log")
    public ResponseEntity<String> logEvent(@RequestBody Map<String, String> payload) {
        String eventType = payload.getOrDefault("eventType", "UNKNOWN");
        String eventData = payload.getOrDefault("eventData", "");
        auditService.log(eventType, eventData);
        return ResponseEntity.ok("logged");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("audit-service is running");
    }
}
