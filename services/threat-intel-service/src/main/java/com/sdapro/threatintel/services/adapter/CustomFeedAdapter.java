package com.sdapro.threatintel.services.adapter;

import com.sdapro.threatintel.domain.reputation.ReputationResult;

// PATTERN: Adapter
// RATIONALE: Organizations maintain proprietary internal threat feeds
// (e.g. homegrown IOC databases, ISACs). CustomFeedAdapter converts
// the internal CSV/JSON feed format into SDA-Pro's canonical
// ReputationResult without changing any consumer code.
// This is the 3rd adapter alongside VirusTotalAdapter and MISPAdapter.
public class CustomFeedAdapter implements ThreatIntelProvider {

    private final String feedUrl;
    private final String feedFormat; // CSV or JSON

    public CustomFeedAdapter(String feedUrl, String feedFormat) {
        this.feedUrl = feedUrl;
        this.feedFormat = feedFormat;
    }

    @Override
    public ReputationResult checkReputation(String indicator, String indicatorType) {
        // Simulate custom internal feed lookup
        CustomFeedResponse feedResponse = queryCustomFeed(indicator, indicatorType);
        return adaptToCanonical(feedResponse, indicator, indicatorType);
    }

    // Simulates internal feed HTTP GET
    private CustomFeedResponse queryCustomFeed(String indicator, String indicatorType) {
        CustomFeedResponse response = new CustomFeedResponse();

        // Simulation: custom feed uses simple ioc_value,threat_type,score format
        if (indicator.contains("evil") || indicator.startsWith("10.0.0.")) {
            response.iocValue = indicator;
            response.threatType = "INTERNAL_BLOCKLIST";
            response.score = 80;
            response.tags = "internal,blocklist,corporate-feed";
            response.found = true;
        } else {
            response.iocValue = indicator;
            response.threatType = "UNKNOWN";
            response.score = 0;
            response.tags = "";
            response.found = false;
        }
        return response;
    }

    // PATTERN: Adapter — core adaptation logic
    // Converts custom feed format → SDA-Pro canonical ReputationResult
    private ReputationResult adaptToCanonical(CustomFeedResponse response,
                                               String indicator, String indicatorType) {
        ReputationResult result = new ReputationResult();
        result.setIndicator(indicator);
        result.setIndicatorType(indicatorType);
        result.setSource("CUSTOM_FEED");

        if (response.found && response.score >= 70) {
            result.setVerdict("MALICIOUS");
            result.setConfidenceScore(response.score);
        } else if (response.found && response.score >= 40) {
            result.setVerdict("SUSPICIOUS");
            result.setConfidenceScore(response.score);
        } else {
            result.setVerdict("CLEAN");
            result.setConfidenceScore(5);
        }

        result.setDetails("CustomFeed feedUrl=" + feedUrl
                + " found=" + response.found
                + " threatType=" + response.threatType
                + " tags=" + response.tags);
        return result;
    }

    // Internal DTO representing raw custom feed response
    private static class CustomFeedResponse {
        String iocValue;
        String threatType;
        int score;
        String tags;
        boolean found;
    }
}
