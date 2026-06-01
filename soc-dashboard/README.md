# SOC Analyst Dashboard

**Owner:** Student C  
**Port:** 3000

## Architecture Style
MVC (Model-View-Controller)

## Structure
- `src/controllers/` — Route handlers, REST/WebSocket request handlers
- `src/models/` — API clients, DTOs, application state
- `src/views/` — React components (AlertStream, IncidentQueue, ResponseConsole, MetricsDashboard)
- `src/services/` — Dashboard-specific business logic
- `src/store/` — State management

## Patterns Used
- MVC: Full separation of Controllers, Models, Views
- Observer: Receives WebSocket push updates from EventBusPublisher

## Features
- Real-time alert stream (updates in under 2 seconds via WebSocket)
- Incident queue with state management
- Manual response action trigger
- Analyst annotation and override
- Metrics dashboard
