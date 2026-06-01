package com.sdapro.alertingestion.controllers;

import com.sdapro.alertingestion.domain.alert.CanonicalAlert;
import com.sdapro.alertingestion.domain.source.AlertSourceType;
import com.sdapro.alertingestion.services.normalizer.AlertNormalizer;
import com.sdapro.alertingestion.services.normalizer.AlertNormalizerFactory;
import com.sdapro.alertingestion.config.IngestionConfigManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// MVC: Controller layer for Alert Ingestion Service
// PATTERN: Factory Method used here to get correct normalizer
@RestController
@RequestMapping("/api/v1/alerts")
public class AlertIngestionController {

    private List<CanonicalAlert> alertStore = new ArrayList<>();

    // POST /api/v1/alerts/ingest/splunk
    @PostMapping("/ingest/splunk")
    public ResponseEntity<CanonicalAlert> ingestSplunkAlert(@RequestBody Map<String, String> payload) {
        IngestionConfigManager config = IngestionConfigManager.getInstance();
        if (!config.isIngestionEnabled(AlertSourceType.SPLUNK)) {
            return ResponseEntity.status(503).build();
        }
        AlertNormalizer normalizer = AlertNormalizerFactory.createNormalizer(AlertSourceType.SPLUNK);
        String rawPayload = payload.getOrDefault("data", "");
        CanonicalAlert alert = normalizer.normalize(rawPayload);
        alertStore.add(alert);
        return ResponseEntity.accepted().body(alert);
    }

    // POST /api/v1/alerts/ingest/crowdstrike
    @PostMapping("/ingest/crowdstrike")
    public ResponseEntity<CanonicalAlert> ingestCrowdStrikeAlert(@RequestBody Map<String, String> payload) {
        IngestionConfigManager config = IngestionConfigManager.getInstance();
        if (!config.isIngestionEnabled(AlertSourceType.CROWDSTRIKE)) {
            return ResponseEntity.status(503).build();
        }
        AlertNormalizer normalizer = AlertNormalizerFactory.createNormalizer(AlertSourceType.CROWDSTRIKE);
        String rawPayload = payload.getOrDefault("data", "");
        CanonicalAlert alert = normalizer.normalize(rawPayload);
        alertStore.add(alert);
        return ResponseEntity.accepted().body(alert);
    }

    // GET /api/v1/alerts
    @GetMapping
    public ResponseEntity<List<CanonicalAlert>> getAllAlerts() {
        return ResponseEntity.ok(alertStore);
    }

    // GET /api/v1/alerts/severity/{severity}
    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<CanonicalAlert>> getAlertsBySeverity(@PathVariable String severity) {
        List<CanonicalAlert> filtered = new ArrayList<>();
        for (CanonicalAlert alert : alertStore) {
            if (alert.getSeverity() != null &&
                alert.getSeverity().name().equalsIgnoreCase(severity)) {
                filtered.add(alert);
            }
        }
        return ResponseEntity.ok(filtered);
    }

    // GET /api/v1/alerts/config
    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getConfig() {
        IngestionConfigManager config = IngestionConfigManager.getInstance();
        Map<String, Object> configData = Map.of(
            "maxBatchSize", config.getMaxBatchSize(),
            "pollingIntervalSeconds", config.getPollingIntervalSeconds(),
            "splunkEnabled", config.isIngestionEnabled(AlertSourceType.SPLUNK),
            "crowdstrikeEnabled", config.isIngestionEnabled(AlertSourceType.CROWDSTRIKE)
        );
        return ResponseEntity.ok(configData);
    }

    // GET /api/v1/alerts/health
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("alert-ingestion-service is running");
    }
}
