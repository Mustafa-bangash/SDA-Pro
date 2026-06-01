package com.sdapro.enrichment.services.pipeline;

// PATTERN: Abstract Factory (Concrete Product)
// RATIONALE: Premium tier ThreatIntel with detailed scoring
public class PremiumThreatIntelService implements ThreatIntelService {

    @Override
    public String checkReputation(String indicator) {
        // Premium tier - detailed reputation with confidence score
        if (indicator.contains("malicious")) return "MALICIOUS - Confidence: 98% - APT29";
        if (indicator.contains("suspicious")) return "SUSPICIOUS - Confidence: 72% - Unknown Actor";
        return "CLEAN - Confidence: 95%";
    }

    @Override
    public String getProviderName() {
        return "Premium-ThreatIntel-VirusTotal";
    }
}
