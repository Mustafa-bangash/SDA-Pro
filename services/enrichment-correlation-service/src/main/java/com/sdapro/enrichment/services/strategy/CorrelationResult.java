package com.sdapro.enrichment.services.strategy;

// PATTERN: Strategy (Result object)
// RATIONALE: Holds the outcome of any correlation strategy
public class CorrelationResult {

    public enum Action {
        CREATE_INCIDENT,
        ADD_TO_EXISTING,
        MONITOR_ONLY,
        DISMISS
    }

    private Action recommendedAction;
    private String reason;
    private String incidentId;
    private double confidenceScore;

    public CorrelationResult(Action action, String reason, double confidence) {
        this.recommendedAction = action;
        this.reason = reason;
        this.confidenceScore = confidence;
    }

    public Action getRecommendedAction() { return recommendedAction; }
    public String getReason() { return reason; }
    public String getIncidentId() { return incidentId; }
    public void setIncidentId(String incidentId) { this.incidentId = incidentId; }
    public double getConfidenceScore() { return confidenceScore; }

    @Override
    public String toString() {
        return "CorrelationResult{action=" + recommendedAction +
               ", reason=" + reason +
               ", confidence=" + confidenceScore + "}";
    }
}
