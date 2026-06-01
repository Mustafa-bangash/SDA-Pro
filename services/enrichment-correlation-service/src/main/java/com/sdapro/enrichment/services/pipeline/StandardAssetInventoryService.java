package com.sdapro.enrichment.services.pipeline;

// PATTERN: Abstract Factory (Concrete Product)
// RATIONALE: Standard tier Asset Inventory implementation
public class StandardAssetInventoryService implements AssetInventoryService {

    @Override
    public String getAssetCriticality(String assetId) {
        // Standard tier - basic asset lookup
        if (assetId.contains("server")) return "HIGH";
        if (assetId.contains("db")) return "CRITICAL";
        return "MEDIUM";
    }

    @Override
    public String getProviderName() {
        return "Standard-AssetInventory";
    }
}
