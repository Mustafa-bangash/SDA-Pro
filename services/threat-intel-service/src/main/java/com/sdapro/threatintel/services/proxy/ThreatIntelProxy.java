package com.sdapro.threatintel.services.proxy;

import com.sdapro.threatintel.domain.reputation.ReputationResult;

// PATTERN: Proxy
// RATIONALE: Defines the contract that all proxy implementations must follow.
// CachingProxy, RateLimitProxy, and AccessControlProxy all implement this
// interface so client code stays decoupled from specific proxy behavior.
public interface ThreatIntelProxy {
    ReputationResult checkReputation(String indicator, String indicatorType);
    String getProviderName();
}
