package com.sdapro.enrichment.domain.enrichment;

// PATTERN: Chain of Responsibility (result passed through handlers)
// RATIONALE: Each handler in the chain adds its enrichment data
// to this result object before passing to the next handler.
public class EnrichmentResult {

    public enum Status {
        COMPLETE,
        PARTIAL,
        FAILED,
        SKIPPED
    }

    private Status status;
    private String geoLocation;
    private String threatIntelScore;
    private String assetCriticality;
    private String classification;
    private boolean isDuplicate;
    private String message;

    public EnrichmentResult() {
        this.status = Status.PARTIAL;
        this.isDuplicate = false;
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getGeoLocation() { return geoLocation; }
    public void setGeoLocation(String geoLocation) { this.geoLocation = geoLocation; }
    public String getThreatIntelScore() { return threatIntelScore; }
    public void setThreatIntelScore(String score) { this.threatIntelScore = score; }
    public String getAssetCriticality() { return assetCriticality; }
    public void setAssetCriticality(String criticality) { this.assetCriticality = criticality; }
    public String getClassification() { return classification; }
    public void setClassification(String classification) { this.classification = classification; }
    public boolean isDuplicate() { return isDuplicate; }
    public void setDuplicate(boolean duplicate) { isDuplicate = duplicate; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public String toString() {
        return "EnrichmentResult{status=" + status +
               ", geoLocation=" + geoLocation +
               ", threatIntelScore=" + threatIntelScore +
               ", classification=" + classification + "}";
    }
}
