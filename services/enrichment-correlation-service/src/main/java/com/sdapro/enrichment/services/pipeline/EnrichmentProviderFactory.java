package com.sdapro.enrichment.services.pipeline;

// PATTERN: Abstract Factory (Factory Interface)
// RATIONALE: Creates families of related enrichment providers.
// Switching from Standard to Premium enrichment only requires
// changing the factory — no other code changes needed.
public interface EnrichmentProviderFactory {
    GeoLocationService createGeoProvider();
    ThreatIntelService createThreatIntelProvider();
    AssetInventoryService createAssetProvider();
    String getFactoryType();
}
