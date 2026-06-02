package com.sdapro.threatintel.services.proxy;

import com.sdapro.threatintel.domain.reputation.ReputationResult;
import com.sdapro.threatintel.services.adapter.ThreatIntelProvider;

// PATTERN: Proxy (Rate Limiting Proxy)
// RATIONALE: External threat intel APIs (VirusTotal free tier = 4 req/min,
// MISP = varies by deployment) enforce rate limits. Without a proxy,
// services would hit 429 errors unpredictably. RateLimitProxy enforces
// a configurable request-per-minute ceiling before delegating to the
// real provider, keeping the consumer code completely unaware of limits.
public class RateLimitProxy implements ThreatIntelProvider {

    private final ThreatIntelProvider realProvider;
    private final int maxRequestsPerMinute;

    // Rate limiting state
    private int requestCount;
    private long windowStartTime;

    public RateLimitProxy(ThreatIntelProvider realProvider, int maxRequestsPerMinute) {
        this.realProvider = realProvider;
        this.maxRequestsPerMinute = maxRequestsPerMinute;
        this.requestCount = 0;
        this.windowStartTime = System.currentTimeMillis();
    }

    @Override
    public ReputationResult checkReputation(String indicator, String indicatorType) {
        // PATTERN: Proxy — intercept before delegating to real provider
        enforceRateLimit();
        return realProvider.checkReputation(indicator, indicatorType);
    }

    // Checks and enforces rate limit window
    private void enforceRateLimit() {
        long now = System.currentTimeMillis();
        long elapsed = now - windowStartTime;

        // Reset window every 60 seconds
        if (elapsed >= 60_000) {
            requestCount = 0;
            windowStartTime = now;
        }

        // Block if limit exceeded
        if (requestCount >= maxRequestsPerMinute) {
            long waitMs = 60_000 - elapsed;
            throw new RateLimitExceededException(
                "Rate limit of " + maxRequestsPerMinute
                + " req/min exceeded. Retry after " + waitMs + "ms. "
                + "Use CachingProxy to reduce external API calls.");
        }

        requestCount++;
    }

    public int getCurrentRequestCount() {
        return requestCount;
    }

    public int getRemainingRequests() {
        return Math.max(0, maxRequestsPerMinute - requestCount);
    }

    // Custom exception for rate limit breach
    public static class RateLimitExceededException extends RuntimeException {
        public RateLimitExceededException(String message) {
            super(message);
        }
    }
}
