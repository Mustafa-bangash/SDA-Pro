package com.sdapro.alertingestion.domain.alert;

import java.util.ArrayList;
import java.util.List;

// PATTERN: Composite (Leaf Node)
// RATIONALE: Represents one individual security alert from any source.
// Leaf nodes do not have children.
public class SingleAlert extends AbstractAlert {

    private String rawPayload;
    private String normalizedData;
    private String sourceIp;
    private String destinationIp;

    public SingleAlert(Severity severity, String sourceType,
                       String rawPayload, String sourceIp, String destinationIp) {
        super(severity, sourceType);
        this.rawPayload = rawPayload;
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
    }

    @Override
    public void add(AlertComponent component) {
        throw new UnsupportedOperationException("SingleAlert is a leaf — cannot add children");
    }

    @Override
    public void remove(AlertComponent component) {
        throw new UnsupportedOperationException("SingleAlert is a leaf — cannot remove children");
    }

    @Override
    public List<AlertComponent> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public boolean isComposite() {
        return false;
    }

    public String getRawPayload() { return rawPayload; }
    public String getNormalizedData() { return normalizedData; }
    public void setNormalizedData(String normalizedData) { this.normalizedData = normalizedData; }
    public String getSourceIp() { return sourceIp; }
    public String getDestinationIp() { return destinationIp; }

    @Override
    public String toString() {
        return "SingleAlert{id=" + id + ", severity=" + severity +
               ", sourceType=" + sourceType + ", sourceIp=" + sourceIp + "}";
    }
}
