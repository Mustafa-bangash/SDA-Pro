# Individual Reflection Report — Student C
## SDA-Pro: Security Incident Response & Threat Mitigation Platform
### Software Design & Architecture — Semester Project

---

## 1. My Role & Responsibilities

As the **SOC Platform Engineer**, I was responsible for the real-time
analyst-facing layer of SDA-Pro. My ownership covered:

- **soc-dashboard** — MVC architecture with Observer-driven updates
- **audit-service** — Immutable compliance audit trail
- **event-bus** — Observer + Singleton infrastructure
- **identity-service** — Analyst authentication stub

---

## 2. Design Patterns — Decisions and Justifications

### 2.1 Observer — EventBusPublisher + Subscribers

**Decision:** I implemented the event bus using the Observer pattern with
EventBusPublisher as the Singleton subject and four concrete observers:
DashboardUpdater, AuditLogger, NotificationDispatcher, MetricsCollector.

**Justification:** The SOC dashboard must update in under 2 seconds when
a new incident is created or a state transition occurs. Without Observer,
the dashboard would need to poll every service every second — thousands
of unnecessary HTTP calls under load. With Observer, services publish
domain events once and all subscribers react simultaneously. The dashboard
never calls incident-management-service directly — it only reacts to
IncidentCreated and IncidentStateChanged events.

**What I learned:** The power of Observer is in decoupling. When we added
the MetricsCollector subscriber, we touched zero existing code — we simply
attached a new observer to the event bus. This is the Open/Closed Principle
in action: open for extension, closed for modification.

**Honest reflection:** The hardest part was deciding event granularity.
Too coarse (one generic "SomethingHappened" event) and subscribers cannot
filter relevantly. Too fine (one event per field change) and the bus becomes
noisy. I settled on domain-level events (AlertIngested, IncidentCreated,
IncidentStateChanged, ResponseActionExecuted) which map naturally to the
incident response lifecycle.

---

### 2.2 Singleton — EventBusPublisher

**Decision:** EventBusPublisher is a Singleton because the event bus must
be a single coordination point. Multiple instances would cause subscribers
registered on one instance to miss events published on another.

**What I learned:** Singleton and Observer work naturally together here.
The Singleton guarantees one subject; Observer guarantees all subscribers
receive every event. Separating them into two patterns made both cleaner
than a combined "global event dispatcher" would have been.

---

### 2.3 MVC — SOC Analyst Dashboard

**Decision:** I structured the dashboard strictly as MVC: Controllers handle
HTTP/WebSocket requests, Models own data and business logic, Views format
output for display.

**Justification:** The dashboard has three distinct concerns — request
handling, data management, and rendering. MVC enforces separation of these
concerns at the architecture level, not just by convention. A Controller
never formats HTML; a View never fetches data; a Model never handles HTTP.

**What I learned:** MVC is most valuable when the View needs to change
without touching business logic. If the dashboard switches from a REST
polling interface to a WebSocket push model, only the Controller and View
change — the Model is untouched. This separation paid off when integrating
the Observer-driven real-time updates into the dashboard.

---

## 3. Architecture Decisions

### Event-Driven Architecture

The most important architectural decision for my components was using
event-driven communication rather than synchronous REST calls between
services. When an incident is created, incident-management-service
publishes an event and continues — it does not wait for the dashboard,
audit service, or notification service to acknowledge. This dramatically
reduces coupling and allows each subscriber to fail independently without
affecting the publisher.

### Observer for Compliance

Using Observer for the audit trail was a deliberate compliance decision.
If the audit service were called directly by each service, a developer
could accidentally omit the audit call. With Observer, every domain event
is automatically logged — there is no way to publish an event without
the AuditLogger seeing it.

---

## 4. Challenges Faced

**Challenge 1 — Real-time update latency:** The teacher's requirement
specifies dashboard updates in under 2 seconds. Achieving this with
WebSocket push via the Observer pattern was straightforward, but ensuring
the DashboardUpdater correctly maps each event type to the right WebSocket
channel required careful design of the onEvent() dispatch logic.

**Challenge 2 — Immutable audit trail:** Ensuring the audit log is truly
immutable (no delete, no update) required returning unmodifiable collections
from AuditService and using append-only storage semantics.

---

## 5. Key Takeaways

- Observer eliminates polling and replaces it with push-based updates,
  which is both more efficient and more real-time.
- MVC enforced clean boundaries between request handling, data, and
  rendering that made the dashboard easy to extend.
- Singleton + Observer is a natural combination for an event bus — one
  subject, many subscribers, zero coupling between publishers and consumers.

---

*Student C — SOC Platform Engineer*
*Software Design & Architecture — 6th Semester*
