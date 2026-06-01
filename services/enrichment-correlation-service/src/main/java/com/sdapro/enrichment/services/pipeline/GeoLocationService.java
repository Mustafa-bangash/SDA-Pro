package com.sdapro.enrichment.services.pipeline;

// PATTERN: Abstract Factory (Product Interface)
// RATIONALE: Defines contract for all GeoLocation providers
public interface GeoLocationService {
    String resolveLocation(String ipAddress);
    String getProviderName();
}
