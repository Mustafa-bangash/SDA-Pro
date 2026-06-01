package com.sdapro.alertingestion.domain.alert;

import java.util.ArrayList;
import java.util.List;

// PATTERN: Composite (Composite Node)
// RATIONALE: Groups correlated alerts into a cluster for unified incident handling.
public class IncidentCluster extends AbstractAlert {

    private String correlationRule;
    private List<AlertComponent> children;

    public IncidentCluster(String correlationRule) {
        super(Severity.LOW, "CLUSTER");
        this.correlationRule = correlationRule;
        this.children = new ArrayList<>();
    }

    @Override
    public void add(AlertComponent component) {
        children.add(component);
        updateSeverity();
    }

    @Override
    public void remove(AlertComponent component) {
        children.remove(component);
        updateSeverity();
    }

    @Override
    public List<AlertComponent> getChildren() {
        return children;
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    private void updateSeverity() {
        this.severity = children.stream()
                .map(AlertComponent::getSeverity)
                .max(Enum::compareTo)
                .orElse(Severity.LOW);
    }

    public String getCorrelationRule() { return correlationRule; }

    @Override
    public String toString() {
        return "IncidentCluster{id=" + id + ", rule=" + correlationRule +
               ", severity=" + severity + ", children=" + children.size() + "}";
    }
}
