package com.sdapro.enrichment.services.pipeline;

import com.sdapro.enrichment.domain.enrichment.EnrichmentResult;

// PATTERN: Chain of Responsibility (Concrete Handler 3)
// RATIONALE: Enriches alert with threat intelligence reputation scores
// Queries ThreatIntelProxy which handles caching and rate limiting
public class ThreatIntelHandler extends EnrichmentHandler {

    @Override
    protected void doEnrich(String alertData, EnrichmentResult result) {
        // PATTERN: Chain of Responsibility
        // Skip if duplicate
        if (result.isDuplicate()) return;

        // In real system this would query ThreatIntelProxy
        String score = resolveThreatIntelScore(alertData);
        result.setThreatIntelScore(score);
    }

    private String resolveThreatIntelScore(String alertData) {
        // Mock threat intel score for demonstration
        if (alertData.contains("malicious")) return "MALICIOUS - Score: 95/100";
        if (alertData.contains("suspicious")) return "SUSPICIOUS - Score: 60/100";
        return "CLEAN - Score: 10/100";
    }
}
