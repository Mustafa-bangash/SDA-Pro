package com.sdapro.alertingestion.config;

import com.sdapro.alertingestion.domain.source.AlertSourceType;
import java.util.HashMap;
import java.util.Map;

// PATTERN: Singleton
// RATIONALE: Single source of truth for all ingestion configuration.
// Ensures all services share the same configuration state without
// creating multiple conflicting instances.
public class IngestionConfigManager {

    // PATTERN: Singleton - single static instance
    private static IngestionConfigManager instance;

    private Map<AlertSourceType, Boolean> enabledSources;
    private Map<AlertSourceType, String> sourceEndpoints;
    private int maxBatchSize;
    private int pollingIntervalSeconds;

    // PATTERN: Singleton - private constructor prevents direct instantiation
    private IngestionConfigManager() {
        enabledSources = new HashMap<>();
        sourceEndpoints = new HashMap<>();

        enabledSources.put(AlertSourceType.SPLUNK, true);
        enabledSources.put(AlertSourceType.CROWDSTRIKE, true);
        enabledSources.put(AlertSourceType.FIREWALL, true);
        enabledSources.put(AlertSourceType.CLOUD_SIEM, false);

        sourceEndpoints.put(AlertSourceType.SPLUNK,
            "http://splunk:8088/services/collector");
        sourceEndpoints.put(AlertSourceType.CROWDSTRIKE,
            "http://crowdstrike:8080/api/alerts");

        this.maxBatchSize = 100;
        this.pollingIntervalSeconds = 30;
    }

    // PATTERN: Singleton - thread-safe lazy initialization
    public static synchronized IngestionConfigManager getInstance() {
        if (instance == null) {
            instance = new IngestionConfigManager();
        }
        return instance;
    }

    public boolean isIngestionEnabled(AlertSourceType sourceType) {
        return enabledSources.getOrDefault(sourceType, false);
    }

    public String getSourceEndpoint(AlertSourceType sourceType) {
        return sourceEndpoints.getOrDefault(sourceType, "");
    }

    public void setIngestionEnabled(AlertSourceType sourceType, boolean enabled) {
        enabledSources.put(sourceType, enabled);
    }

    public int getMaxBatchSize() { return maxBatchSize; }
    public void setMaxBatchSize(int maxBatchSize) { this.maxBatchSize = maxBatchSize; }
    public int getPollingIntervalSeconds() { return pollingIntervalSeconds; }
    public void setPollingIntervalSeconds(int seconds) {
        this.pollingIntervalSeconds = seconds;
    }
}
