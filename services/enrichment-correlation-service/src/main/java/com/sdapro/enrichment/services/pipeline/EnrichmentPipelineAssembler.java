package com.sdapro.enrichment.services.pipeline;

// PATTERN: Chain of Responsibility (Pipeline Assembler)
// RATIONALE: Builds and assembles the enrichment chain in correct order.
// Deduplication -> GeoIP -> ThreatIntel -> AssetContext -> Classification
public class EnrichmentPipelineAssembler {

    public static EnrichmentHandler assemblePipeline() {
        // PATTERN: Chain of Responsibility
        // Build the chain: H1 -> H2 -> H3 -> H4 -> H5
        DeduplicationHandler deduplication = new DeduplicationHandler();
        GeoIPHandler geoIP = new GeoIPHandler();
        ThreatIntelHandler threatIntel = new ThreatIntelHandler();
        AssetContextHandler assetContext = new AssetContextHandler();
        ClassificationHandler classification = new ClassificationHandler();

        // Link the chain
        deduplication
            .setNext(geoIP)
            .setNext(threatIntel)
            .setNext(assetContext)
            .setNext(classification);

        // Return first handler - entry point of the chain
        return deduplication;
    }
}
