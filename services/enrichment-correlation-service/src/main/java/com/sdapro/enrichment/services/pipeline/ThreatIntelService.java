package com.sdapro.enrichment.services.pipeline;

// PATTERN: Abstract Factory (Product Interface)
// RATIONALE: Defines contract for all ThreatIntel providers
public interface ThreatIntelService {
    String checkReputation(String indicator);
    String getProviderName();
}
