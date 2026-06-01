# Alert Processing Middleware

**Owner:** Student B (primary), Student A (supporting)  

## Pattern Implemented
Chain of Responsibility — configurable alert processing pipeline

## Pipeline Stages
1. `deduplication/` — Handler 1: Deduplicate repeated alerts
2. `enrichment/` — Handler 2: Enrich with geo, threat intel, asset context
3. `classification/` — Handler 3: Classify severity
4. `routing/` — Handler 4: Route to appropriate service

## How It Works
Each handler either processes the alert and passes it to the next handler,
or stops the chain (e.g. duplicate detected). The pipeline is assembled
dynamically by `EnrichmentPipelineAssembler`.
