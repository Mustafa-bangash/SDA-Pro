package com.sdapro.enrichment.services.pipeline;

// PATTERN: Abstract Factory (Concrete Product)
// RATIONALE: Premium tier Asset Inventory with full CMDB integration
public class PremiumAssetInventoryService implements AssetInventoryService {

    @Override
    public String getAssetCriticality(String assetId) {
        // Premium tier - full CMDB lookup with business context
        if (assetId.contains("server")) return "HIGH - Revenue Impact: $50k/hr";
        if (assetId.contains("db")) return "CRITICAL - PII Data - Regulatory Impact";
        return "MEDIUM - Standard Business Asset";
    }

    @Override
    public String getProviderName() {
        return "Premium-AssetInventory-ServiceNow";
    }
}
