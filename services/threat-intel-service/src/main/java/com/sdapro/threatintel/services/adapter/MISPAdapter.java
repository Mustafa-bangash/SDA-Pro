package com.sdapro.threatintel.services.adapter;

import com.sdapro.threatintel.domain.reputation.ReputationResult;

// PATTERN: Adapter
// RATIONALE: MISP uses a completely different API format than VirusTotal.
// MISPAdapter converts MISP's attribute-based response into SDA-Pro's
// canonical ReputationResult without changing any consumer code.
public class MISPAdapter implements ThreatIntelProvider {

    // PATTERN: Adapter — adaptee (simulated MISP client)
    private final String mispBaseUrl;
    private final String mispApiKey;

    public MISPAdapter(String mispBaseUrl, String mispApiKey) {
        this.mispBaseUrl = mispBaseUrl;
        this.mispApiKey = mispApiKey;
    }

    @Override
    public ReputationResult checkReputation(String indicator, String indicatorType) {
        // Simulate MISP attribute lookup response
        // Real impl: POST /attributes/restSearch with authkey header
        MISPAttributeResponse mispResponse = queryMISP(indicator, indicatorType);
        return adaptToCanonical(mispResponse, indicator, indicatorType);
    }

    // Simulates MISP REST API call
    private MISPAttributeResponse queryMISP(String indicator, String indicatorType) {
        // MISP returns attributes with category, type, value, and to_ids flag
        MISPAttributeResponse response = new MISPAttributeResponse();

        // Simulation: known malicious indicators
        if (indicator.startsWith("185.") || indicator.contains("malware")) {
            response.category = "Network activity";
            response.type = indicatorType.toLowerCase();
            response.value = indicator;
            response.toIds = true;           // to_ids=true means confirmed malicious
            response.eventCount = 12;        // seen in 12 MISP events
            response.threatLevel = 1;        // 1=High, 2=Medium, 3=Low, 4=Undefined
        } else {
            response.category = "External analysis";
            response.type = indicatorType.toLowerCase();
            response.value = indicator;
            response.toIds = false;
            response.eventCount = 0;
            response.threatLevel = 4;
        }
        return response;
    }

    // PATTERN: Adapter — core adaptation logic
    // Converts MISP format → SDA-Pro canonical ReputationResult
    private ReputationResult adaptToCanonical(MISPAttributeResponse mispResponse,
                                               String indicator, String indicatorType) {
        ReputationResult result = new ReputationResult();
        result.setIndicator(indicator);
        result.setIndicatorType(indicatorType);
        result.setSource("MISP");

        // Adapt MISP threat level to canonical verdict
        if (mispResponse.toIds && mispResponse.threatLevel == 1) {
            result.setVerdict("MALICIOUS");
            result.setConfidenceScore(90);
        } else if (mispResponse.toIds && mispResponse.threatLevel == 2) {
            result.setVerdict("SUSPICIOUS");
            result.setConfidenceScore(65);
        } else if (mispResponse.eventCount > 0) {
            result.setVerdict("SUSPICIOUS");
            result.setConfidenceScore(40);
        } else {
            result.setVerdict("CLEAN");
            result.setConfidenceScore(10);
        }

        result.setDetails("MISP events=" + mispResponse.eventCount
                + " toIds=" + mispResponse.toIds
                + " threatLevel=" + mispResponse.threatLevel
                + " category=" + mispResponse.category);
        return result;
    }

    // Internal DTO representing raw MISP API response structure
    private static class MISPAttributeResponse {
        String category;
        String type;
        String value;
        boolean toIds;
        int eventCount;
        int threatLevel;
    }
}
