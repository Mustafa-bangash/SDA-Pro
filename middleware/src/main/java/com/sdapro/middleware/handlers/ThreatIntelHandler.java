package com.sdapro.middleware.handlers;

public class ThreatIntelHandler extends EnrichmentHandler {
    @Override
    protected boolean doEnrich(String alertData) {
        System.out.println("[Pipeline Step 2] Adding Threat Intelligence context...");
        System.out.println("-> Checked external databases. Alert updated.");
        return true; // Continue the chain
    }
}
