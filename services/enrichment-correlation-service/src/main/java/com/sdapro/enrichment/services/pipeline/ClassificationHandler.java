package com.sdapro.enrichment.services.pipeline;

import com.sdapro.enrichment.domain.enrichment.EnrichmentResult;

// PATTERN: Chain of Responsibility (Concrete Handler 5 - Last in chain)
// RATIONALE: Final handler that classifies the alert severity
// based on all previously enriched data from other handlers
public class ClassificationHandler extends EnrichmentHandler {

    @Override
    protected void doEnrich(String alertData, EnrichmentResult result) {
        // PATTERN: Chain of Responsibility
        // Skip if duplicate
        if (result.isDuplicate()) return;

        // Classify based on enriched data from previous handlers
        String classification = classify(result);
        result.setClassification(classification);
    }

    private String classify(EnrichmentResult result) {
        // Rule-based classification using enriched data
        String threatScore = result.getThreatIntelScore();
        String assetCriticality = result.getAssetCriticality();

        if (threatScore != null && threatScore.contains("MALICIOUS")) {
            if (assetCriticality != null && assetCriticality.contains("CRITICAL")) {
                return "CRITICAL - Immediate Response Required";
            }
            return "HIGH - Urgent Response Required";
        }
        if (threatScore != null && threatScore.contains("SUSPICIOUS")) {
            return "MEDIUM - Investigation Required";
        }
        return "LOW - Monitor and Log";
    }
}
