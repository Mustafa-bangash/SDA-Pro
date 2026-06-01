package com.sdapro.enrichment.services.pipeline;

import com.sdapro.enrichment.domain.enrichment.EnrichmentResult;

// PATTERN: Chain of Responsibility (Abstract Handler)
// RATIONALE: Defines the chain linking mechanism and delegates
// to concrete handlers. Each handler decides whether to process
// the request or pass it to the next handler in the chain.
public abstract class EnrichmentHandler {

    private EnrichmentHandler nextHandler;

    // PATTERN: Chain of Responsibility
    // Links this handler to the next one in the chain
    public EnrichmentHandler setNext(EnrichmentHandler handler) {
        this.nextHandler = handler;
        return handler;
    }

    // PATTERN: Chain of Responsibility
    // Template method - calls doEnrich then passes to next handler
    public EnrichmentResult handle(String alertData, EnrichmentResult result) {
        doEnrich(alertData, result);
        if (nextHandler != null) {
            return nextHandler.handle(alertData, result);
        }
        result.setStatus(EnrichmentResult.Status.COMPLETE);
        return result;
    }

    // Each concrete handler implements this method
    protected abstract void doEnrich(String alertData, EnrichmentResult result);
}
