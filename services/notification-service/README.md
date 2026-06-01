# Notification Service

**Owner:** Student B (primary), Student C (supporting)  
**Port:** 8086

## Patterns Implemented
- Abstract Factory: `EnterpriseNotificationFactory`, `BasicNotificationFactory`
  - Creates families of notifiers: Email + Slack + PagerDuty

## Responsibilities
- Dispatches notifications to analysts via multiple channels
- Selects notification factory based on enterprise vs basic tier
- Subscribes to `IncidentCreated`, `IncidentStateChanged`, `ResponseActionExecuted` events

## API Endpoints
- `POST /api/v1/notifications/dispatch` — send notification
- `GET /api/v1/notifications/history` — get notification history
- `GET /api/v1/notifications/health` — health check
