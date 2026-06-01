# Response Orchestration Service

**Owner:** Student B  
**Port:** 8084

## Patterns Implemented
- Facade: `IncidentResponseFacade` — simplifies complex response subsystem
- Strategy: `AggressiveContainmentStrategy`, `BalancedResponseStrategy`, `ConservativeStrategy`
- Decorator: `AuditLogDecorator`, `ApprovalGateDecorator`, `RollbackCapabilityDecorator`, `MetricsDecorator`

## Responsibilities
- Receives incident ID and determines appropriate response plan
- Selects response strategy based on severity and asset criticality
- Executes response actions decorated with audit logging and approval gates
- Publishes `ResponseActionExecuted` event to RabbitMQ

## API Endpoints
- `POST /api/v1/response/{incidentId}/assess` — assess and respond
- `GET /api/v1/response/{incidentId}/history` — get response history
- `POST /api/v1/response/{incidentId}/rollback` — rollback last action
- `GET /api/v1/response/health` — health check
