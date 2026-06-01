# Identity Service

**Owner:** Student C (primary), Student B (supporting)  
**Port:** 8088

## Responsibilities
- Manages SOC analyst authentication and authorization
- Issues and validates session tokens (stored in Redis)
- Enforces role-based access control for response actions

## API Endpoints
- `POST /api/v1/identity/login` — analyst login
- `POST /api/v1/identity/logout` — analyst logout
- `GET /api/v1/identity/analysts/{id}` — get analyst profile
- `GET /api/v1/identity/health` — health check
