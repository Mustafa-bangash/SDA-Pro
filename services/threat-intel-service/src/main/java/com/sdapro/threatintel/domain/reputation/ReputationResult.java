package com.sdapro.threatintel.domain.reputation;

public class ReputationResult {
    private String indicator;
    private String verdict;
    private int confidenceScore;

    public ReputationResult(String indicator, String verdict, int confidenceScore) {
        this.indicator = indicator;
        this.verdict = verdict;
        this.confidenceScore = confidenceScore;
    }

    public String getIndicator() { return indicator; }
    public String getVerdict() { return verdict; }
    public int getConfidenceScore() { return confidenceScore; }
}
