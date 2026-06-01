# Event Bus Infrastructure

**Owner:** Student C (primary), Student B (supporting)

## Patterns Implemented
- Observer: Publisher/Subscriber infrastructure for all domain events
- Singleton: `EventBusPublisher` — single event coordination point

## Structure
- `src/publisher/` — `EventBusPublisher` (Singleton) — publishes events to RabbitMQ
- `src/subscribers/` — `DashboardUpdater`, `AuditLogger`, `NotificationDispatcher`, `MetricsCollector`
- `src/schemas/` — Event type definitions and JSON schemas

## Events Flowing Through Bus
| Event | Publisher | Subscribers |
|-------|-----------|-------------|
| AlertIngested | alert-ingestion-service | enrichment, dashboard, audit |
| AlertEnriched | enrichment-correlation-service | correlation, dashboard, audit |
| IncidentCreated | incident-management-service | dashboard, notification, response |
| IncidentStateChanged | incident-management-service | dashboard, audit, metrics |
| ResponseActionExecuted | response-orchestration-service | dashboard, audit, notification |
| ThreatIntelUpdated | threat-intel-service | enrichment (cache invalidation) |
