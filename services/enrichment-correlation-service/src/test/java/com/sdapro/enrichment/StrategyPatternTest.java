package com.sdapro.enrichment;

import com.sdapro.enrichment.domain.enrichment.EnrichmentResult;
import com.sdapro.enrichment.services.strategy.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// PATTERN: Strategy
public class StrategyPatternTest {

    @Test
    public void testAggressiveStrategyCreatesIncidentForMediumSeverity() {
        // Aggressive strategy should create incident for non-low severity
        AggressiveCorrelationStrategy strategy = new AggressiveCorrelationStrategy();
        EnrichmentResult result = new EnrichmentResult();
        result.setClassification("MEDIUM - Investigation Required");

        CorrelationResult correlation = strategy.correlate("test-alert", result);
        assertEquals(CorrelationResult.Action.CREATE_INCIDENT, correlation.getRecommendedAction());
    }

    @Test
    public void testConservativeStrategyOnlyCreatesIncidentForConfirmedCritical() {
        // Conservative strategy requires MALICIOUS + CRITICAL to create incident
        ConservativeCorrelationStrategy strategy = new ConservativeCorrelationStrategy();
        EnrichmentResult result = new EnrichmentResult();
        result.setThreatIntelScore("MALICIOUS - Score: 95/100");
        result.setClassification("CRITICAL - Immediate Response Required");

        CorrelationResult correlation = strategy.correlate("test-alert", result);
        assertEquals(CorrelationResult.Action.CREATE_INCIDENT, correlation.getRecommendedAction());
    }

    @Test
    public void testConservativeStrategyMonitorsLowSeverity() {
        // Conservative strategy should only monitor low severity alerts
        ConservativeCorrelationStrategy strategy = new ConservativeCorrelationStrategy();
        EnrichmentResult result = new EnrichmentResult();
        result.setThreatIntelScore("CLEAN - Score: 10/100");
        result.setClassification("LOW - Monitor and Log");

        CorrelationResult correlation = strategy.correlate("test-alert", result);
        assertEquals(CorrelationResult.Action.MONITOR_ONLY, correlation.getRecommendedAction());
    }

    @Test
    public void testBalancedStrategyCreatesIncidentForMaliciousOnCriticalAsset() {
        // Balanced strategy should create incident for malicious on critical asset
        BalancedCorrelationStrategy strategy = new BalancedCorrelationStrategy();
        EnrichmentResult result = new EnrichmentResult();
        result.setThreatIntelScore("MALICIOUS - Score: 95/100");
        result.setAssetCriticality("CRITICAL - Database Server");
        result.setClassification("CRITICAL - Immediate Response Required");

        CorrelationResult correlation = strategy.correlate("test-alert", result);
        assertEquals(CorrelationResult.Action.CREATE_INCIDENT, correlation.getRecommendedAction());
    }

    @Test
    public void testStrategySelectorPicksAggressiveForCriticalAsset() {
        // Selector should pick aggressive strategy for critical asset
        CorrelationStrategySelector selector = new CorrelationStrategySelector();
        EnrichmentResult result = new EnrichmentResult();
        result.setAssetCriticality("CRITICAL - Database Server");
        result.setThreatIntelScore("MALICIOUS - Score: 95/100");

        selector.selectStrategy(result);
        assertEquals("AGGRESSIVE_CORRELATION", selector.getCurrentStrategyName());
    }

    @Test
    public void testStrategySelectorPicksConservativeForCleanAlert() {
        // Selector should pick conservative strategy for clean threat score
        CorrelationStrategySelector selector = new CorrelationStrategySelector();
        EnrichmentResult result = new EnrichmentResult();
        result.setAssetCriticality("LOW - User Workstation");
        result.setThreatIntelScore("CLEAN - Score: 10/100");

        selector.selectStrategy(result);
        assertEquals("CONSERVATIVE_CORRELATION", selector.getCurrentStrategyName());
    }
}
