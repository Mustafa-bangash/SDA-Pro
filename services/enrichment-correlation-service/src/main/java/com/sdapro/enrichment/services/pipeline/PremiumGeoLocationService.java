package com.sdapro.enrichment.services.pipeline;

// PATTERN: Abstract Factory (Concrete Product)
// RATIONALE: Premium tier GeoLocation with more detailed resolution
public class PremiumGeoLocationService implements GeoLocationService {

    @Override
    public String resolveLocation(String ipAddress) {
        // Premium tier - detailed city level resolution
        if (ipAddress.startsWith("192.168") || ipAddress.startsWith("10.0")) {
            return "Internal Network - HQ Building";
        }
        return "External - Geo: US/New York (Premium Tier)";
    }

    @Override
    public String getProviderName() {
        return "Premium-GeoIP-MaxMind";
    }
}
