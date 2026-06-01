package com.sdapro.enrichment.services.pipeline;

// PATTERN: Abstract Factory (Concrete Product)
// RATIONALE: Standard tier GeoLocation implementation
public class StandardGeoLocationService implements GeoLocationService {

    @Override
    public String resolveLocation(String ipAddress) {
        // Standard tier - basic country level resolution
        if (ipAddress.startsWith("192.168") || ipAddress.startsWith("10.0")) {
            return "Internal Network";
        }
        return "External - Country Unknown (Standard Tier)";
    }

    @Override
    public String getProviderName() {
        return "Standard-GeoIP";
    }
}
