package com.sdapro.middleware.handlers;

// PATTERN: Chain of Responsibility
// RATIONALE: Decouples alert processing stages. Allows us to dynamically add, 
// remove, or reorder pipeline steps without breaking the system.
public abstract class EnrichmentHandler {
    protected EnrichmentHandler nextHandler;

    public EnrichmentHandler setNext(EnrichmentHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public void handle(String alertData) {
        boolean shouldContinue = doEnrich(alertData);
        
        // Only pass to the next worker if current worker says it is okay to continue
        if (shouldContinue && nextHandler != null) {
            nextHandler.handle(alertData);
        }
    }

    // Each specific worker will define their own job here
    protected abstract boolean doEnrich(String alertData);
}
