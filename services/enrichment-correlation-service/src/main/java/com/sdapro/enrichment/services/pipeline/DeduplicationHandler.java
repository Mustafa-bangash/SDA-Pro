package com.sdapro.enrichment.services.pipeline;

import com.sdapro.enrichment.domain.enrichment.EnrichmentResult;
import java.util.HashSet;
import java.util.Set;

// PATTERN: Chain of Responsibility (Concrete Handler 1)
// RATIONALE: First handler in chain, checks for duplicate alerts
// before passing to expensive enrichment handlers
public class DeduplicationHandler extends EnrichmentHandler {

    private Set<String> processedAlerts = new HashSet<>();

    @Override
    protected void doEnrich(String alertData, EnrichmentResult result) {
        // PATTERN: Chain of Responsibility
        if (processedAlerts.contains(alertData)) {
            result.setDuplicate(true);
            result.setMessage("Duplicate alert detected, skipping enrichment");
            result.setStatus(EnrichmentResult.Status.SKIPPED);
        } else {
            processedAlerts.add(alertData);
            result.setDuplicate(false);
        }
    }
}
