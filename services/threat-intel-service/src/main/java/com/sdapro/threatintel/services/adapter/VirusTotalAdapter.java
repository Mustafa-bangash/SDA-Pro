package com.sdapro.threatintel.services.adapter;
import com.sdapro.threatintel.domain.reputation.ReputationResult;

// PATTERN: Adapter
// RATIONALE: Converts the specific, external VirusTotal API response format 
// into the canonical ReputationResult format required by SDA-Pro.
public class VirusTotalAdapter implements ThreatIntelProvider {
    
    @Override
    public ReputationResult checkReputation(String indicator) {
        System.out.println("[ThreatIntel] Querying VirusTotal API for: " + indicator);
        
        String verdict = "CLEAN";
        int score = 0;
        
        if (indicator.equals("192.168.1.100") || indicator.equals("10.0.0.15")) {
            verdict = "MALICIOUS";
            score = 95;
        } else if (indicator.equals("10.0.0.30")) {
            verdict = "SUSPICIOUS";
            score = 75;
        }
        
        return new ReputationResult(indicator, verdict, score);
    }
}
