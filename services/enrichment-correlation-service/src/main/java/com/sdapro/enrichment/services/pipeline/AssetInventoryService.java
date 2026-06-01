package com.sdapro.enrichment.services.pipeline;

// PATTERN: Abstract Factory (Product Interface)
// RATIONALE: Defines contract for all Asset Inventory providers
public interface AssetInventoryService {
    String getAssetCriticality(String assetId);
    String getProviderName();
}
