package com.sdapro.middleware.handlers;

public class DeduplicationHandler extends EnrichmentHandler {
    @Override
    protected boolean doEnrich(String alertData) {
        System.out.println("[Pipeline Step 1] Checking for duplicates...");
        
        if (alertData.contains("duplicate=true")) {
            System.out.println("-> Duplicate found! Stopping the assembly line here.");
            return false; // Stop the chain
        }
        
        System.out.println("-> Alert is unique. Passing to next step.");
        return true; // Continue the chain
    }
}
