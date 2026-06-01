package com.sdapro.enrichment.controllers;

import com.sdapro.enrichment.domain.enrichment.EnrichmentResult;
import com.sdapro.enrichment.services.pipeline.EnrichmentHandler;
import com.sdapro.enrichment.services.pipeline.EnrichmentPipelineAssembler;
import com.sdapro.enrichment.services.strategy.CorrelationResult;
import com.sdapro.enrichment.services.strategy.CorrelationStrategySelector;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// MVC: Controller layer for Enrichment Correlation Service
// PATTERN: Chain of Responsibility pipeline triggered here
// PATTERN: Strategy pattern for correlation triggered here
@RestController
@RequestMapping("/api/v1/enrichment")
public class EnrichmentController {

    private List<EnrichmentResult> enrichmentHistory = new ArrayList<>();

    // POST /api/v1/enrichment/enrich
    // Runs alert data through the full enrichment pipeline
    @PostMapping("/enrich")
    public ResponseEntity<EnrichmentResult> enrichAlert(@RequestBody Map<String, String> payload) {
        String alertData = payload.getOrDefault("alertData", "");

        // PATTERN: Chain of Responsibility - assemble and run pipeline
        EnrichmentHandler pipeline = EnrichmentPipelineAssembler.assemblePipeline();
        EnrichmentResult result = new EnrichmentResult();
        pipeline.handle(alertData, result);

        enrichmentHistory.add(result);
        return ResponseEntity.ok(result);
    }

    // POST /api/v1/enrichment/correlate
    // Runs correlation strategy on enriched alert data
    @PostMapping("/correlate")
    public ResponseEntity<CorrelationResult> correlateAlert(@RequestBody Map<String, String> payload) {
        String alertData = payload.getOrDefault("alertData", "");

        // First enrich
        EnrichmentHandler pipeline = EnrichmentPipelineAssembler.assemblePipeline();
        EnrichmentResult enrichmentResult = new EnrichmentResult();
        pipeline.handle(alertData, enrichmentResult);

        // PATTERN: Strategy - select and execute correlation strategy
        CorrelationStrategySelector selector = new CorrelationStrategySelector();
        CorrelationResult correlationResult = selector.executeStrategy(alertData, enrichmentResult);

        return ResponseEntity.ok(correlationResult);
    }

    // GET /api/v1/enrichment/history
    // Returns all enrichment results
    @GetMapping("/history")
    public ResponseEntity<List<EnrichmentResult>> getEnrichmentHistory() {
        return ResponseEntity.ok(enrichmentHistory);
    }

    // GET /api/v1/enrichment/health
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("enrichment-correlation-service is running");
    }
}
