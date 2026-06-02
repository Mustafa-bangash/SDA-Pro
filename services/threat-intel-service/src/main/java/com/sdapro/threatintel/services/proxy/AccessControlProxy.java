package com.sdapro.threatintel.services.proxy;

import com.sdapro.threatintel.domain.reputation.ReputationResult;
import com.sdapro.threatintel.services.adapter.ThreatIntelProvider;

// PATTERN: Proxy (Access Control Proxy)
// RATIONALE: Not every service should be allowed to query threat intel
// directly. Only enrichment-correlation-service and response-orchestration-service
// are authorized consumers. AccessControlProxy enforces this authorization
// before delegating to the real provider, without modifying provider code.
// This is the 3rd proxy alongside CachingProxy and RateLimitProxy.
public class AccessControlProxy implements ThreatIntelProvider {

    private final ThreatIntelProvider realProvider;
    private final String[] authorizedCallers;

    public AccessControlProxy(ThreatIntelProvider realProvider, String... authorizedCallers) {
        this.realProvider = realProvider;
        this.authorizedCallers = authorizedCallers;
    }

    @Override
    public ReputationResult checkReputation(String indicator, String indicatorType) {
        // PATTERN: Proxy — intercept and enforce authorization before delegating
        String caller = resolveCallerIdentity();
        if (!isAuthorized(caller)) {
            throw new UnauthorizedAccessException(
                "Caller '" + caller + "' is not authorized to query threat intel. "
                + "Only enrichment-correlation-service and response-orchestration-service are permitted.");
        }
        return realProvider.checkReputation(indicator, indicatorType);
    }

    // Resolves the calling service identity (simulated via thread name in test)
    private String resolveCallerIdentity() {
        // Real impl: extract service identity from JWT token or mTLS certificate
        String threadName = Thread.currentThread().getName();
        if (threadName.contains("enrichment")) return "enrichment-correlation-service";
        if (threadName.contains("response")) return "response-orchestration-service";
        return "unknown-caller";
    }

    private boolean isAuthorized(String caller) {
        for (String authorized : authorizedCallers) {
            if (authorized.equals(caller)) return true;
        }
        return false;
    }

    // Custom exception for unauthorized access attempts
    public static class UnauthorizedAccessException extends RuntimeException {
        public UnauthorizedAccessException(String message) {
            super(message);
        }
    }
}
