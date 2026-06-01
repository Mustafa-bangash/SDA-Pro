package com.sdapro.enrichment.services.pipeline;

import com.sdapro.enrichment.domain.enrichment.EnrichmentResult;

// PATTERN: Chain of Responsibility (Concrete Handler 4)
// RATIONALE: Enriches alert with asset criticality information
// Determines how critical the affected asset is to the business
public class AssetContextHandler extends EnrichmentHandler {

    @Override
    protected void doEnrich(String alertData, EnrichmentResult result) {
        // PATTERN: Chain of Responsibility
        // Skip if duplicate
        if (result.isDuplicate()) return;

        // In real system this would query Asset Inventory Service
        String criticality = resolveAssetCriticality(alertData);
        result.setAssetCriticality(criticality);
    }

    private String resolveAssetCriticality(String alertData) {
        // Mock asset criticality resolution for demonstration
        if (alertData.contains("server")) return "HIGH - Production Server";
        if (alertData.contains("database")) return "CRITICAL - Database Server";
        if (alertData.contains("workstation")) return "LOW - User Workstation";
        return "MEDIUM - Unknown Asset";
    }
}
