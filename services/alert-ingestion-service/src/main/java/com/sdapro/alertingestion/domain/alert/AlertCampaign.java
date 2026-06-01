package com.sdapro.alertingestion.domain.alert;

import java.util.ArrayList;
import java.util.List;

// PATTERN: Composite (Composite Node)
// RATIONALE: Groups multiple alerts into a multi-stage attack campaign.
// Treated uniformly with SingleAlert for enrichment and response.
public class AlertCampaign extends AbstractAlert {

    private String campaignName;
    private String attackPattern;
    private List<AlertComponent> children;

    public AlertCampaign(String campaignName, String attackPattern) {
        super(Severity.LOW, "CAMPAIGN");
        this.campaignName = campaignName;
        this.attackPattern = attackPattern;
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

    // PATTERN: Composite
    // Campaign severity = highest severity among its children
    private void updateSeverity() {
        this.severity = children.stream()
                .map(AlertComponent::getSeverity)
                .max(Enum::compareTo)
                .orElse(Severity.LOW);
    }

    public String getCampaignName() { return campaignName; }
    public String getAttackPattern() { return attackPattern; }

    @Override
    public String toString() {
        return "AlertCampaign{id=" + id + ", name=" + campaignName +
               ", severity=" + severity + ", children=" + children.size() + "}";
    }
}
