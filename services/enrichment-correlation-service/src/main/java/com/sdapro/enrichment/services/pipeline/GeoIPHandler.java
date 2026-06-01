package com.sdapro.enrichment.services.pipeline;

import com.sdapro.enrichment.domain.enrichment.EnrichmentResult;

// PATTERN: Chain of Responsibility (Concrete Handler 2)
// RATIONALE: Enriches alert with geographic location data
// based on source IP address
public class GeoIPHandler extends EnrichmentHandler {

    @Override
    protected void doEnrich(String alertData, EnrichmentResult result) {
        // PATTERN: Chain of Responsibility
        // Skip if duplicate
        if (result.isDuplicate()) return;

        // In real system this would call a GeoIP service
        String geoLocation = resolveGeoLocation(alertData);
        result.setGeoLocation(geoLocation);
    }

    private String resolveGeoLocation(String alertData) {
        // Mock GeoIP resolution for demonstration
        if (alertData.contains("192.168")) return "Internal Network";
        if (alertData.contains("10.0")) return "Internal Network";
        return "External - Unknown Location";
    }
}
