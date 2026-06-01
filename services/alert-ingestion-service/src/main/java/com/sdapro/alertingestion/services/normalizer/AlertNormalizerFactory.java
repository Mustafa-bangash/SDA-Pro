package com.sdapro.alertingestion.services.normalizer;

import com.sdapro.alertingestion.domain.source.AlertSourceType;
import java.util.HashMap;
import java.util.Map;

// PATTERN: Factory Method
// RATIONALE: Decouples alert ingestion logic from specific normalizer
// implementations. New sources can be added by registering a new
// normalizer without changing any existing code (Open/Closed Principle).
public class AlertNormalizerFactory {

    private static final Map<AlertSourceType, AlertNormalizer> normalizers = new HashMap<>();

    static {
        register(AlertSourceType.SPLUNK, new SplunkNormalizer());
        register(AlertSourceType.CROWDSTRIKE, new CrowdStrikeNormalizer());
        register(AlertSourceType.FIREWALL, new FirewallNormalizer());
    }

    public static void register(AlertSourceType type, AlertNormalizer normalizer) {
        normalizers.put(type, normalizer);
    }

    public static AlertNormalizer createNormalizer(AlertSourceType sourceType) {
        AlertNormalizer normalizer = normalizers.get(sourceType);
        if (normalizer == null) {
            throw new IllegalArgumentException(
                "No normalizer registered for source type: " + sourceType);
        }
        return normalizer;
    }
}
