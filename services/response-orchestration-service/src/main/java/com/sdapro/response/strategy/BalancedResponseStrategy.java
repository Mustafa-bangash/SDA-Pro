package com.sdapro.response.strategy;

// PATTERN: Strategy (Concrete Strategy 3)
// RATIONALE: Balanced approach weighing both containment speed and
// business continuity. Used for HIGH severity incidents where aggressive
// containment may disrupt operations but doing nothing is not acceptable.
public class BalancedResponseStrategy implements ResponseStrategy {

    @Override
    public String determineActions(String incidentId, String severity, String assetCriticality) {
        // PATTERN: Strategy
        // Balance containment with minimal business disruption
        if ("HIGH".equals(severity) && "CRITICAL".equals(assetCriticality)) {
            return "BALANCED: Block IP + Notify Tier-2 + Schedule maintenance window for isolation";
        }
        if ("HIGH".equals(severity)) {
            return "BALANCED: Block IP + Increase monitoring + Notify SOC lead";
        }
        if ("MEDIUM".equals(severity)) {
            return "BALANCED: Increase monitoring + Notify analyst + Schedule investigation";
        }
        return "BALANCED: Log and monitor - no immediate action required";
    }

    @Override
    public String getName() {
        return "BALANCED_RESPONSE";
    }

    @Override
    public String getDescription() {
        return "Balances containment effectiveness with business continuity impact";
    }
}
