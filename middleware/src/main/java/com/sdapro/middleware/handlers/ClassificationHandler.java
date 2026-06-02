package com.sdapro.middleware.handlers;

public class ClassificationHandler extends EnrichmentHandler {
    @Override
    protected boolean doEnrich(String alertData) {
        System.out.println("[Pipeline Step 3] Classifying severity...");
        System.out.println("-> Alert classified as CRITICAL. Ready for response orchestration.");
        return true; // End of chain
    }
}
