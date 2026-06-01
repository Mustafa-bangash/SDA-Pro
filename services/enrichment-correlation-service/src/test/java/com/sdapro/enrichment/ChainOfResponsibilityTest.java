package com.sdapro.enrichment;

import com.sdapro.enrichment.domain.enrichment.EnrichmentResult;
import com.sdapro.enrichment.services.pipeline.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// PATTERN: Chain of Responsibility
public class ChainOfResponsibilityTest {

    @Test
    public void testPipelineCompletesSuccessfully() {
        // Full pipeline should complete with COMPLETE status
        EnrichmentHandler pipeline = EnrichmentPipelineAssembler.assemblePipeline();
        EnrichmentResult result = new EnrichmentResult();
        pipeline.handle("src_ip=192.168.1.1,suspicious=true,server=prod", result);
        assertEquals(EnrichmentResult.Status.COMPLETE, result.getStatus());
    }

    @Test
    public void testDeduplicationHandlerDetectsDuplicate() {
        // Same alert data should be marked as duplicate on second run
        DeduplicationHandler handler = new DeduplicationHandler();
        EnrichmentResult result1 = new EnrichmentResult();
        EnrichmentResult result2 = new EnrichmentResult();

        handler.doEnrich("duplicate-alert-data", result1);
        assertFalse(result1.isDuplicate());

        handler.doEnrich("duplicate-alert-data", result2);
        assertTrue(result2.isDuplicate());
    }

    @Test
    public void testGeoIPHandlerEnrichesLocation() {
        // GeoIP handler should set geo location
        GeoIPHandler handler = new GeoIPHandler();
        EnrichmentResult result = new EnrichmentResult();
        handler.doEnrich("src_ip=192.168.1.1", result);
        assertNotNull(result.getGeoLocation());
        assertEquals("Internal Network", result.getGeoLocation());
    }

    @Test
    public void testThreatIntelHandlerDetectsMalicious() {
        // ThreatIntel handler should detect malicious indicator
        ThreatIntelHandler handler = new ThreatIntelHandler();
        EnrichmentResult result = new EnrichmentResult();
        handler.doEnrich("malicious=true,src_ip=1.2.3.4", result);
        assertNotNull(result.getThreatIntelScore());
        assertTrue(result.getThreatIntelScore().contains("MALICIOUS"));
    }

    @Test
    public void testClassificationHandlerClassifiesCritical() {
        // Classification handler should classify as CRITICAL
        // when threat is malicious and asset is critical
        ClassificationHandler handler = new ClassificationHandler();
        EnrichmentResult result = new EnrichmentResult();
        result.setThreatIntelScore("MALICIOUS - Score: 95/100");
        result.setAssetCriticality("CRITICAL - Database Server");
        handler.doEnrich("test", result);
        assertNotNull(result.getClassification());
        assertTrue(result.getClassification().contains("CRITICAL"));
    }

    @Test
    public void testPipelineSkipsEnrichmentForDuplicates() {
        // If alert is duplicate, pipeline should skip enrichment
        EnrichmentHandler pipeline = EnrichmentPipelineAssembler.assemblePipeline();
        String alertData = "unique-alert-12345";

        EnrichmentResult first = new EnrichmentResult();
        pipeline.handle(alertData, first);
        assertFalse(first.isDuplicate());

        EnrichmentResult second = new EnrichmentResult();
        pipeline.handle(alertData, second);
        assertTrue(second.isDuplicate());
    }
}
