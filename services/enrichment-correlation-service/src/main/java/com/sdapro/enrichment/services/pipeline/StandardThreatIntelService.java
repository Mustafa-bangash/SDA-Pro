package com.sdapro.enrichment.services.pipeline;

// PATTERN: Abstract Factory (Concrete Product)
// RATIONALE: Standard tier ThreatIntel implementation
public class StandardThreatIntelService implements ThreatIntelService {

    @Override
    public String checkReputation(String indicator) {
        // Standard tier - basic reputation check
        if (indicator.contains("malicious")) return "MALICIOUS";
        if (indicator.contains("suspicious")) return "SUSPICIOUS";
        return "UNKNOWN";
    }

    @Override
    public String getProviderName() {
        return "Standard-ThreatIntel";
    }
}
