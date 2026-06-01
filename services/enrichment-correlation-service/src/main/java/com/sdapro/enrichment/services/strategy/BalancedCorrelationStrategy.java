package com.sdapro.enrichment.services.strategy;

import com.sdapro.enrichment.domain.enrichment.EnrichmentResult;

// PATTERN: Strategy (Concrete Strategy 3)
// RATIONALE: Balanced approach - default strategy that weighs
// both false positives and false negatives equally
public class BalancedCorrelationStrategy implements CorrelationStrategy {

    @Override
    public CorrelationResult correlate(String alertData, EnrichmentResult enrichmentResult) {
        // PATTERN: Strategy
        // Balanced - consider both threat intel and asset criticality
        String threatScore = enrichmentResult.getThreatIntelScore();
        String assetCriticality = enrichmentResult.getAssetCriticality();
        String classification = enrichmentResult.getClassification();

        boolean isMalicious = threatScore != null && threatScore.contains("MALICIOUS");
        boolean isCriticalAsset = assetCriticality != null && assetCriticality.contains("CRITICAL");
        boolean isHighSeverity = classification != null &&
                (classification.contains("CRITICAL") || classification.contains("HIGH"));

        if (isMalicious && isCriticalAsset) {
            return new CorrelationResult(
                CorrelationResult.Action.CREATE_INCIDENT,
                "Balanced strategy - malicious threat on critical asset",
                0.97
            );
        }
        if (isHighSeverity) {
            return new CorrelationResult(
                CorrelationResult.Action.CREATE_INCIDENT,
                "Balanced strategy - high severity alert",
                0.85
            );
        }
        return new CorrelationResult(
            CorrelationResult.Action.MONITOR_ONLY,
            "Balanced strategy - below incident threshold",
            0.65
        );
    }

    @Override
    public String getStrategyName() {
        return "BALANCED_CORRELATION";
    }
}
