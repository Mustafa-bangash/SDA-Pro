package com.sdapro.response.strategy;

// PATTERN: Strategy (Concrete Strategy 4)
// RATIONALE: Conservative monitoring-only approach. Used for LOW severity
// incidents or when automated response could cause more damage than the
// threat itself (e.g. blocking a critical business partner IP).
public class WatchAndWaitStrategy implements ResponseStrategy {

    @Override
    public String determineActions(String incidentId, String severity, String assetCriticality) {
        // PATTERN: Strategy
        // Observe and collect evidence without taking disruptive action
        if ("CRITICAL".equals(assetCriticality)) {
            return "WATCH: Enhanced logging + Passive monitoring + Alert human analyst - NO automated action on critical asset";
        }
        if ("LOW".equals(severity)) {
            return "WATCH: Log event + Add to watchlist + Review in next daily standup";
        }
        return "WATCH: Monitor traffic patterns + Collect forensic evidence + Await analyst decision";
    }

    @Override
    public String getName() {
        return "WATCH_AND_WAIT";
    }

    @Override
    public String getDescription() {
        return "Passive monitoring strategy - observe and collect evidence without disrupting operations";
    }
}
