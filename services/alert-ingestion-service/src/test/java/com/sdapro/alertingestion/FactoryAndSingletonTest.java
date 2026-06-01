package com.sdapro.alertingestion;

import com.sdapro.alertingestion.domain.alert.CanonicalAlert;
import com.sdapro.alertingestion.domain.alert.Severity;
import com.sdapro.alertingestion.domain.source.AlertSourceType;
import com.sdapro.alertingestion.services.normalizer.*;
import com.sdapro.alertingestion.config.IngestionConfigManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// PATTERN: Factory Method, Singleton
public class FactoryAndSingletonTest {

    @Test
    public void testFactoryReturnsCorrectNormalizerForSplunk() {
        // Factory Method should return SplunkNormalizer for SPLUNK source
        AlertNormalizer normalizer = AlertNormalizerFactory.createNormalizer(AlertSourceType.SPLUNK);
        assertNotNull(normalizer);
        assertTrue(normalizer.supports(AlertSourceType.SPLUNK));
        assertFalse(normalizer.supports(AlertSourceType.CROWDSTRIKE));
    }

    @Test
    public void testFactoryReturnsCorrectNormalizerForCrowdStrike() {
        // Factory Method should return CrowdStrikeNormalizer for CROWDSTRIKE source
        AlertNormalizer normalizer = AlertNormalizerFactory.createNormalizer(AlertSourceType.CROWDSTRIKE);
        assertNotNull(normalizer);
        assertTrue(normalizer.supports(AlertSourceType.CROWDSTRIKE));
        assertFalse(normalizer.supports(AlertSourceType.SPLUNK));
    }

    @Test
    public void testFactoryThrowsForUnregisteredSource() {
        // Factory should throw for unregistered source type
        assertThrows(IllegalArgumentException.class, () ->
            AlertNormalizerFactory.createNormalizer(AlertSourceType.FIREWALL)
        );
    }

    @Test
    public void testSplunkNormalizerProducesCanonicalAlert() {
        // SplunkNormalizer should produce a valid CanonicalAlert
        SplunkNormalizer normalizer = new SplunkNormalizer();
        String rawPayload = "src_ip=192.168.1.1,dest_ip=10.0.0.1,severity=high,event_type=LOGIN_FAILURE,message=Failed login attempt";
        CanonicalAlert alert = normalizer.normalize(rawPayload);

        assertNotNull(alert);
        assertNotNull(alert.getId());
        assertEquals("SPLUNK", alert.getSourceType());
        assertEquals("192.168.1.1", alert.getSourceIp());
        assertEquals(Severity.HIGH, alert.getSeverity());
    }

    @Test
    public void testCrowdStrikeNormalizerProducesCanonicalAlert() {
        // CrowdStrikeNormalizer should produce a valid CanonicalAlert
        CrowdStrikeNormalizer normalizer = new CrowdStrikeNormalizer();
        String rawPayload = "device_ip=10.0.0.5,target_ip=192.168.1.100,severity_level=85,detection_type=MALWARE,description=Ransomware detected";
        CanonicalAlert alert = normalizer.normalize(rawPayload);

        assertNotNull(alert);
        assertEquals("CROWDSTRIKE", alert.getSourceType());
        assertEquals(Severity.CRITICAL, alert.getSeverity());
    }

    @Test
    public void testSingletonReturnsSameInstance() {
        // Singleton should always return the same instance
        IngestionConfigManager instance1 = IngestionConfigManager.getInstance();
        IngestionConfigManager instance2 = IngestionConfigManager.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testSingletonDefaultConfig() {
        // Singleton should have correct default configuration
        IngestionConfigManager config = IngestionConfigManager.getInstance();
        assertTrue(config.isIngestionEnabled(AlertSourceType.SPLUNK));
        assertTrue(config.isIngestionEnabled(AlertSourceType.CROWDSTRIKE));
        assertEquals(100, config.getMaxBatchSize());
    }
}
