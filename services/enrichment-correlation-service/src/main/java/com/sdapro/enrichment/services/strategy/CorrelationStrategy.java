package com.sdapro.enrichment.services.strategy;

import com.sdapro.enrichment.domain.enrichment.EnrichmentResult;

// PATTERN: Strategy (Strategy Interface)
// RATIONALE: Different correlation algorithms can be swapped
// at runtime based on alert type and business context
// without changing the enrichment service code.
public interface CorrelationStrategy {
    CorrelationResult correlate(String alertData, EnrichmentResult enrichmentResult);
    String getStrategyName();
}
