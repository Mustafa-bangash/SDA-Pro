# Threat Intel Service

**Owner:** Student B  
**Port:** 8085

## Patterns Implemented
- Adapter: `VirusTotalAdapter`, `MISPAdapter`, `CustomFeedAdapter` — normalize external API formats
- Proxy: `CachingProxy`, `RateLimitProxy`, `AccessControlProxy` — control access to external APIs

## Responsibilities
- Checks IP, domain, and file hash reputation against external providers
- Caches results in Redis with TTL (see ADR-004)
- Rate limits external API calls to stay within quotas
- Publishes `ThreatIntelUpdated` event when new intel arrives

## API Endpoints
- `POST /api/v1/threat-intel/reputation` — check indicator reputation
- `GET /api/v1/threat-intel/cache/stats` — view cache hit/miss stats
- `DELETE /api/v1/threat-intel/cache/{indicator}` — invalidate cache entry
- `GET /api/v1/threat-intel/health` — health check
