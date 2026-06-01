package com.sdapro.threatintel.services.adapter;
import com.sdapro.threatintel.domain.reputation.ReputationResult;

// PATTERN: Adapter
// RATIONALE: Converts the open-source MISP Threat Sharing API format into our system's Canonical ReputationResult.
public class MISPAdapter implements ThreatIntelProvider {
    
    @Override
    public ReputationResult checkReputation(String indicator) {
        System.out.println("[ThreatIntel] Querying MISP Platform for: " + indicator);
        
        // Simulating the MISP response
        return new ReputationResult(indicator, "SUSPICIOUS", 60);
    }
}
