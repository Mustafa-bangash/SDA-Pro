package com.sdapro.alertingestion;

import com.sdapro.alertingestion.domain.alert.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// PATTERN: Composite
// Tests that SingleAlert, AlertCampaign, IncidentCluster
// are all treated uniformly via AlertComponent interface
public class CompositePatternTest {

    @Test
    public void testSingleAlertIsLeaf() {
        // SingleAlert is a leaf node - cannot have children
        SingleAlert alert = new SingleAlert(
            Severity.HIGH, "SPLUNK", "raw payload", "192.168.1.1", "10.0.0.1"
        );
        assertFalse(alert.isComposite());
        assertTrue(alert.getChildren().isEmpty());
    }

    @Test
    public void testSingleAlertCannotAddChildren() {
        SingleAlert alert = new SingleAlert(
            Severity.HIGH, "SPLUNK", "raw payload", "192.168.1.1", "10.0.0.1"
        );
        SingleAlert child = new SingleAlert(
            Severity.LOW, "SPLUNK", "another payload", "10.0.0.2", "10.0.0.3"
        );
        // Leaf node should throw exception when adding children
        assertThrows(UnsupportedOperationException.class, () -> alert.add(child));
    }

    @Test
    public void testAlertCampaignIsComposite() {
        // AlertCampaign is a composite node - can have children
        AlertCampaign campaign = new AlertCampaign("APT29", "lateral-movement");
        assertTrue(campaign.isComposite());
        assertTrue(campaign.getChildren().isEmpty());
    }

    @Test
    public void testAlertCampaignSeverityUpdatesWithChildren() {
        // Campaign severity should equal highest child severity
        AlertCampaign campaign = new AlertCampaign("APT29", "lateral-movement");
        assertEquals(Severity.LOW, campaign.getSeverity());

        SingleAlert lowAlert = new SingleAlert(
            Severity.LOW, "SPLUNK", "payload1", "192.168.1.1", "10.0.0.1"
        );
        SingleAlert criticalAlert = new SingleAlert(
            Severity.CRITICAL, "CROWDSTRIKE", "payload2", "192.168.1.2", "10.0.0.2"
        );

        campaign.add(lowAlert);
        assertEquals(Severity.LOW, campaign.getSeverity());

        campaign.add(criticalAlert);
        // After adding CRITICAL alert, campaign severity should be CRITICAL
        assertEquals(Severity.CRITICAL, campaign.getSeverity());
    }

    @Test
    public void testIncidentClusterGroupsAlerts() {
        // IncidentCluster groups correlated alerts uniformly
        IncidentCluster cluster = new IncidentCluster("same-source-ip");
        SingleAlert alert1 = new SingleAlert(
            Severity.HIGH, "SPLUNK", "payload1", "192.168.1.1", "10.0.0.1"
        );
        SingleAlert alert2 = new SingleAlert(
            Severity.MEDIUM, "FIREWALL", "payload2", "192.168.1.1", "10.0.0.2"
        );
        cluster.add(alert1);
        cluster.add(alert2);
        assertEquals(2, cluster.getChildren().size());
        assertEquals(Severity.HIGH, cluster.getSeverity());
    }

    @Test
    public void testUniformTreatmentViaInterface() {
        // Both SingleAlert and AlertCampaign treated as AlertComponent
        AlertComponent leaf = new SingleAlert(
            Severity.HIGH, "SPLUNK", "payload", "1.1.1.1", "2.2.2.2"
        );
        AlertComponent composite = new AlertCampaign("test-campaign", "test-pattern");

        // Both have getId, getSeverity, getTimestamp via interface
        assertNotNull(leaf.getId());
        assertNotNull(composite.getId());
        assertNotNull(leaf.getSeverity());
        assertNotNull(composite.getSeverity());
    }
}
