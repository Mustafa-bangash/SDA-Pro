package com.sdapro.enrichment.services.strategy;

import com.sdapro.enrichment.domain.enrichment.EnrichmentResult;

// PATTERN: Strategy (Context/Selector)
// RATIONALE: Selects the appropriate correlation strategy at runtime
// based on the enriched alert data. Client code only depends
// on this selector, not on specific strategy implementations.
public class CorrelationStrategySelector {

    private CorrelationStrategy strategy;

    public CorrelationStrategySelector() {
        // Default strategy is balanced
        this.strategy = new BalancedCorrelationStrategy();
    }

    // PATTERN: Strategy - switch strategy at runtime
    public void setStrategy(CorrelationStrategy strategy) {
        this.strategy = strategy;
    }

    // PATTERN: Strategy - select strategy based on enrichment result
    public CorrelationStrategy selectStrategy(EnrichmentResult result) {
        String assetCriticality = result.getAssetCriticality();
        String threatScore = result.getThreatIntelScore();

        // Select strategy based on context
        if (assetCriticality != null && assetCriticality.contains("CRITICAL")) {
            this.strategy = new AggressiveCorrelationStrategy();
        } else if (threatScore != null && threatScore.contains("CLEAN")) {
            this.strategy = new ConservativeCorrelationStrategy();
        } else {
            this.strategy = new BalancedCorrelationStrategy();
        }
        return this.strategy;
    }

    public CorrelationResult executeStrategy(String alertData, EnrichmentResult result) {
        selectStrategy(result);
        return strategy.correlate(alertData, result);
    }

    public String getCurrentStrategyName() {
        return strategy.getStrategyName();
    }
}
