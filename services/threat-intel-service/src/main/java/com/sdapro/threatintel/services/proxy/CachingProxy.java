package com.sdapro.threatintel.services.proxy;
import com.sdapro.threatintel.services.adapter.ThreatIntelProvider;
import com.sdapro.threatintel.domain.reputation.ReputationResult;
import java.util.HashMap;
import java.util.Map;

// PATTERN: Proxy
// RATIONALE: Controls access to the expensive external ThreatIntelProvider by 
// maintaining a local cache, preventing rate-limiting from external APIs.
public class CachingProxy implements ThreatIntelProvider {
    
    private final ThreatIntelProvider realProvider;
    private final Map<String, ReputationResult> cache;

    public CachingProxy(ThreatIntelProvider realProvider) {
        this.realProvider = realProvider;
        this.cache = new HashMap<>();
    }

    @Override
    public ReputationResult checkReputation(String indicator) {
        if (cache.containsKey(indicator)) {
            System.out.println("[Proxy] Cache hit for " + indicator + ". Skipping external API call.");
            return cache.get(indicator);
        }

        System.out.println("[Proxy] Cache miss for " + indicator + ". Delegating to real provider.");
        ReputationResult result = realProvider.checkReputation(indicator);
        cache.put(indicator, result);
        
        return result;
    }
}
