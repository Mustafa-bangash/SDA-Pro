package com.sdapro.enrichment.services.pipeline;

// PATTERN: Abstract Factory (Concrete Factory 1)
// RATIONALE: Creates the standard tier family of enrichment providers.
// Used for lower priority alerts or cost-sensitive deployments.
public class StandardEnrichmentFactory implements EnrichmentProviderFactory {

    @Override
    public GeoLocationService createGeoProvider() {
        return new StandardGeoLocationService();
    }

    @Override
    public ThreatIntelService createThreatIntelProvider() {
        return new StandardThreatIntelService();
    }

    @Override
    public AssetInventoryService createAssetProvider() {
        return new StandardAssetInventoryService();
    }

    @Override
    public String getFactoryType() {
        return "STANDARD";
    }
}
