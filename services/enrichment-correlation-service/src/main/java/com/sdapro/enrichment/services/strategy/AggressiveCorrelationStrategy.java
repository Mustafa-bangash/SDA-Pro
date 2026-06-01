package com.sdapro.enrichment.services.strategy;

import com.sdapro.enrichment.domain.enrichment.EnrichmentResult;

// PATTERN: Strategy (Concrete Strategy 1)
// RATIONALE: Creates incidents aggressively - used when asset
// criticality is high and false positives are acceptable
public class AggressiveCorrelationStrategy implements CorrelationStrategy {

    @Override
    public CorrelationResult correlate(String alertData, EnrichmentResult enrichmentResult) {
        // PATTERN: Strategy
        // Aggressive - create incident for almost everything
        String classification = enrichmentResult.getClassification();

        if (classification != null && !classification.contains("LOW")) {
            return new CorrelationResult(
                CorrelationResult.Action.CREATE_INCIDENT,
                "Aggressive strategy - creating incident for non-low severity",
                0.95
            );
        }
        return new CorrelationResult(
            CorrelationResult.Action.MONITOR_ONLY,
            "Aggressive strategy - low severity, monitoring only",
            0.70
        );
    }

    @Override
    public String getStrategyName() {
        return "AGGRESSIVE_CORRELATION";
    }
}
