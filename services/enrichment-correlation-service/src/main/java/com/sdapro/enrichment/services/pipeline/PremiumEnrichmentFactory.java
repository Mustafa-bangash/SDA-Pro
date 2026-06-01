package com.sdapro.enrichment.services.pipeline;

// PATTERN: Abstract Factory (Concrete Factory 2)
// RATIONALE: Creates the premium tier family of enrichment providers.
// Used for high severity alerts requiring maximum context.
public class PremiumEnrichmentFactory implements EnrichmentProviderFactory {

    @Override
    public GeoLocationService createGeoProvider() {
        return new PremiumGeoLocationService();
    }

    @Override
    public ThreatIntelService createThreatIntelProvider() {
        return new PremiumThreatIntelService();
    }

    @Override
    public AssetInventoryService createAssetProvider() {
        return new PremiumAssetInventoryService();
    }

    @Override
    public String getFactoryType() {
        return "PREMIUM";
    }
}
