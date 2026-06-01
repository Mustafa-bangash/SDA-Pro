package com.sdapro.enrichment.services.strategy;

import com.sdapro.enrichment.domain.enrichment.EnrichmentResult;

// PATTERN: Strategy (Concrete Strategy 2)
// RATIONALE: Creates incidents conservatively - used when
// false positive rate must be kept very low
public class ConservativeCorrelationStrategy implements CorrelationStrategy {

    @Override
    public CorrelationResult correlate(String alertData, EnrichmentResult enrichmentResult) {
        // PATTERN: Strategy
        // Conservative - only create incident for critical threats
        String threatScore = enrichmentResult.getThreatIntelScore();
        String classification = enrichmentResult.getClassification();

        if (threatScore != null && threatScore.contains("MALICIOUS") &&
            classification != null && classification.contains("CRITICAL")) {
            return new CorrelationResult(
                CorrelationResult.Action.CREATE_INCIDENT,
                "Conservative strategy - confirmed malicious critical threat",
                0.99
            );
        }
        return new CorrelationResult(
            CorrelationResult.Action.MONITOR_ONLY,
            "Conservative strategy - insufficient evidence for incident",
            0.60
        );
    }

    @Override
    public String getStrategyName() {
        return "CONSERVATIVE_CORRELATION";
    }
}
