# Audit Service

**Owner:** Student C  
**Port:** 8087

## Patterns Implemented
- Observer: `AuditEventLogger` — subscribes to all domain events and writes immutable audit trail

## Responsibilities
- Immutably logs every alert ingestion, enrichment, state transition, and response action
- Provides compliance reporting for GDPR and SOC2
- Subscribes to ALL domain events from RabbitMQ

## API Endpoints
- `GET /api/v1/audit/logs` — get audit log entries
- `GET /api/v1/audit/logs/incident/{id}` — get audit trail for specific incident
- `GET /api/v1/audit/compliance/report` — generate compliance report
- `GET /api/v1/audit/health` — health check
