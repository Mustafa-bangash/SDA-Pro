SDA-Pro: Security Incident Response & Threat Mitigation Platform

Software Design & Architecture - Semester Project Final Report

1. Executive Summary

Project Name: SDA-Pro Intelligent Security Operations Center (SOC) Platform
Duration: Semester Project
Domain Context: Cybersecurity automation

SDA-Pro is an automated cybersecurity platform. Modern companies get thousands of security alerts every day from firewalls and antivirus tools, which is impossible for humans to handle manually. We built this system to automatically take in those alerts, clean them up, check external intelligence databases to see how dangerous they are, and automatically fight back against the threats (like blocking a hacker's IP address).

The main goal of this project was to prove our understanding of enterprise software design by strictly implementing 4 Architectural Styles and 12 Design Patterns.

2. Team Structure & Responsibilities

The workload was strictly divided among our three group members to ensure everyone contributed equally to the design patterns and system architecture.

Student A: Mustafa khan (53312) - Threat Pipeline Engineer
Built the systems that bring alerts in and organize them. Responsible for the Alert Ingestion Service, Enrichment Pipeline, and Incident Management Service.
(Focus: Composite, State, Factory Method, Singleton)

Student B: Ahsan Abbas (53143) - Integration & Response Engineer
Built the "muscle" of the platform that fights back. Responsible for the Response Orchestration, Threat Intel integration, and Notification Service.
(Focus: Facade, Strategy, Decorator, Adapter, Proxy, Chain of Responsibility, Abstract Factory)

Student C: Shaheer Fasil (53038) - SOC Platform Engineer
Built the user interface and the communication backbone. Responsible for the SOC Analyst Dashboard, Event Bus, Audit Service, and Identity Service.
(Focus: MVC, Observer, Event-Driven architecture)

3. Core Architectural Styles (The 4 Pillars)

Our system is built using four main architectural styles. Here is how they work in simple terms:

Service-Oriented Architecture (SOA): Instead of building one massive, tangled application, we broke our system into smaller, independent "services" (like a Threat Intel Service and a Notification Service). This means if one service crashes or needs an update, the rest of the system keeps working smoothly.

Model-View-Controller (MVC): Used for our Analyst Dashboard. It strictly separates the data (Model), the user interface the analyst sees (View), and the brain that connects them (Controller). This keeps the frontend code clean and organized.

Layered Architecture: Inside every single service, we stacked our code in layers. The "Presentation" layer handles the incoming web requests, the "Business Logic" layer does the actual thinking and processing, and the "Data Access" layer talks to the database. A layer can only talk to the layer directly beneath it.

Event-Driven Architecture: Instead of services constantly asking each other for updates and slowing the system down, they use an "Event Bus" (RabbitMQ). When the Ingestion service gets an alert, it simply shouts "New Alert!" into the bus. Any service that cares about that news (like the Dashboard) hears it and updates automatically.

4. The 12 Design Patterns Implemented

We successfully implemented 12 specific design patterns across the platform to solve common coding problems.

Creational Patterns (How objects are made)

Singleton (IngestionConfigManager): The Single Source of Truth. We used this to make sure there is only one configuration file running at a time. If different parts of the system used different settings, it would cause chaos.

Factory Method (AlertNormalizerFactory): The Manufacturer. Alerts come in from different tools (Splunk, Firewalls) in messy formats. Instead of writing messy if/else rules, the Factory automatically looks at the source and builds the correct "Normalizer" tool to clean up the data.

Abstract Factory (NotificationFactory): The Combo Meal Maker. We used this to group notifications. If a user is on the "Enterprise" tier, the factory automatically sets up Email, Slack, and PagerDuty all at once, rather than creating them one by one.

Structural Patterns (How parts fit together)

Composite (Alerts & Campaigns): The Tree Structure. Sometimes hackers trigger one alert, sometimes they trigger 50 related alerts (a campaign). This pattern lets our system treat a massive group of alerts exactly the same way it treats a single alert, saving us from writing duplicate code.

Facade (IncidentResponseFacade): The Front Desk Receptionist. Responding to a threat is complicated (checking rules, blocking IPs, logging data). The Facade hides all that mess. It gives the rest of the application one simple assessAndRespond command to use, while it handles the hard work behind the scenes.

Adapter (VirusTotalAdapter): The Travel Plug Adapter. External threat databases like VirusTotal send data in weird, unique formats. The Adapter takes that messy outside data and translates it into the standard format our internal system understands.

Decorator (AuditLogDecorator): The Pizza Toppings. We used this to add extra features to an action without breaking the original code. For example, our base action is "Block an IP". The Decorator wraps around that action to automatically add a security log to the database every time a block happens.

Proxy (CachingProxy): The Bouncer. Outside APIs (like VirusTotal) will block us if we ask them for information too fast. The Proxy sits in front of the API. If we already looked up an IP address 10 seconds ago, the Proxy just hands back the saved answer from its memory, saving us time and avoiding rate limits.

Behavioral Patterns (How objects behave and talk)

State (Incident Lifecycle): The Lifecycle Manager. A security incident goes through phases (New -> Triage -> Containment -> Closed). This pattern ensures that we cannot accidentally skip a step. If an incident is "Closed", the pattern physically blocks anyone from trying to contain it again.

Chain of Responsibility (EnrichmentPipeline): The Assembly Line. Instead of a massive block of code to process an alert, we built an assembly line. Worker 1 checks for duplicates. Worker 2 checks the location. Worker 3 classifies the severity. The alert is passed down the line smoothly.

Strategy (ResponseStrategy): The Playbook. We cannot treat every threat the same. If a threat is a false alarm, the system uses the ConservativeStrategy (just monitor it). If it is a real attack, it automatically swaps to the AggressiveContainmentStrategy (block the IP).

Observer (EventBusPublisher): The News Broadcaster. When an alert is resolved, the Observer automatically broadcasts that news to the Dashboard so the screen updates instantly without the user having to refresh the page.

5. Architecture Decision Records (ADRs)

As a team, we made 5 key architectural decisions to ensure the system is stable and scalable:

ADR-001 (SOA vs Microservices): We chose Service-Oriented Architecture (SOA) over pure Microservices. With a 3-person team, managing dozens of tiny microservices would have been too complex. SOA gave us independent services without the heavy maintenance overhead.

ADR-002 (Hybrid Communication): We used standard REST APIs when a user specifically clicks a button (synchronous). We used Asynchronous Messaging (RabbitMQ) for background tasks (like processing 10,000 alerts a minute) so the system doesn't freeze.

ADR-003 (Database Strategy): We chose PostgreSQL (Relational) because security incidents need strict tracking and relationships. We used Redis (In-Memory) for fast caching.

ADR-004 (Threat Intel Caching): We decided to cache Threat Intel scores in Redis. This acts as a protective barrier to prevent our system from getting banned by external APIs for making too many requests.

ADR-005 (Real-Time Push): We chose WebSockets to push updates to the SOC Analyst Dashboard. This allows the dashboard to show new attacks in real-time (under 2 seconds) without forcing the browser to constantly refresh.

6. UML Diagrams

(Note: These diagrams represent the visual blueprints of our system's architecture. They are written in Mermaid code and will render automatically when viewed on GitHub or compatible Markdown readers.)

6.1 Comprehensive Class Diagram (Design Patterns Overview)

This diagram illustrates the object-oriented structure of our system, highlighting where key design patterns are implemented in the code.

classDiagram
  %% Creational Patterns
  class IngestionConfigManager {
    <<Singleton>>
    -instance
    +getInstance()
  }
  class AlertNormalizerFactory {
    <<Factory Method>>
    +createNormalizer(source)
  }
  class NotificationFactory {
    <<Abstract Factory>>
    +createEmailNotifier()
    +createSlackNotifier()
  }

  %% Structural Patterns
  class IncidentResponseFacade {
    <<Facade>>
    +assessAndRespond(severity)
  }
  class ThreatIntelProvider {
    <<Target Interface>>
    +checkReputation()
  }
  class VirusTotalAdapter {
    <<Adapter>>
  }
  class CachingProxy {
    <<Proxy>>
  }
  class AuditLogDecorator {
    <<Decorator>>
    +execute()
  }

  %% Behavioral Patterns
  class EnrichmentHandler {
    <<Chain of Responsibility>>
    +setNext()
    +handle()
  }
  class ResponseStrategy {
    <<Strategy>>
    +determineActions()
  }
  class EventBusPublisher {
    <<Observer>>
    +publishEvent()
  }
  class IncidentState {
    <<State>>
    +transition()
  }

  ThreatIntelProvider <|.. VirusTotalAdapter
  ThreatIntelProvider <|.. CachingProxy


6.2 Component Diagram (SOA Architecture)

This diagram displays the Service-Oriented Architecture boundaries, showing how the different microservices, message bus, and databases connect to each other.

graph TD
  subgraph Frontend ["SOC Analyst Dashboard (MVC)"]
    UI[React UI / View]
    Ctrl[API Controller]
  end

  subgraph EventBus ["Message Broker"]
    RabbitMQ((RabbitMQ \nEvent Bus))
  end

  subgraph Microservices ["Service-Oriented Architecture (SOA)"]
    Ingestion[Alert Ingestion Service]
    Enrichment[Enrichment Pipeline]
    IncidentMgmt[Incident Management Service]
    Response[Response Orchestration Service]
    ThreatIntel[Threat Intel Service]
    Notification[Notification Service]
  end

  subgraph Databases ["Data Layer"]
    PG[(PostgreSQL)]
    Redis[(Redis Cache)]
  end

  UI <-->|REST / WebSockets| Ctrl
  Ctrl <--> IncidentMgmt
  Ctrl <--> Response

  Ingestion -->|Publish Alert| RabbitMQ
  RabbitMQ -->|Consume| Enrichment
  Enrichment --> IncidentMgmt
  IncidentMgmt -->|Publish Event| RabbitMQ

  Response -->|Query| ThreatIntel
  ThreatIntel <--> Redis
  Response --> Notification

  IncidentMgmt <--> PG
  Response <--> PG


6.3 Sequence Diagram: Alert Ingestion & Enrichment

This sequence traces the exact timeline of an incoming alert as it moves from the SIEM, through the Factory Method normalizers, down the Chain of Responsibility pipeline, and into the State manager.

sequenceDiagram
  participant Source as SIEM / Firewall
  participant Ingest as Ingestion Service
  participant Factory as Normalizer Factory
  participant Pipeline as Enrichment Pipeline (Chain)
  participant State as Incident State Manager

  Source->>Ingest: Send Raw Security Alert
  Ingest->>Factory: request Normalizer(sourceType)
  Factory-->>Ingest: return Correct Normalizer
  Ingest->>Ingest: normalizeData()
  Ingest->>Pipeline: startEnrichment(Alert)
  Note over Pipeline: Chain: Dedupe -> ThreatIntel -> Classify
  Pipeline->>Pipeline: execute Chain of Responsibility
  Pipeline->>State: create/update Incident
  State-->>State: transitionState(New -> Triage)
  State-->>Ingest: Incident Saved Successfully


6.4 Sequence Diagram: Incident Response Orchestration

This timeline shows how an analyst triggers a response, causing the Facade to select a Strategy, wrap it in Decorators, and execute the action via an Adapter.

sequenceDiagram
  actor Analyst
  participant Facade as Incident Response Facade
  participant Strategy as Response Strategy
  participant Decorator as Audit Log Decorator
  participant Adapter as ThreatIntel Adapter
  participant EventBus as RabbitMQ (Observer)

  Analyst->>Facade: assessAndRespond(incidentId, severity)
  Facade->>Strategy: determineActions(severity)
  Strategy-->>Facade: return Actions (e.g., BlockIP)
  Facade->>Decorator: wrapAction(BlockIPAction)
  Decorator->>Adapter: execute Action (via external API)
  Adapter-->>Decorator: success
  Decorator->>Decorator: saveAuditLog()
  Decorator-->>Facade: action complete
  Facade->>EventBus: publishEvent(ResponseActionExecuted)
  EventBus-->>Analyst: WebSocket UI Update


7. Individual Team Reflections

Student A: Mustafa khan (53312) - Threat Pipeline Engineer

The biggest takeaway from building the ingestion pipeline was understanding how patterns reduce complex if/else statements. The Factory Method allowed me to add a new Firewall alert source without changing any existing code, proving the Open/Closed Principle works in real life. The State pattern was incredibly useful—it took a messy 200-line function of conditional checks and turned it into clean, self-contained lifecycle phases, preventing invalid actions (like trying to triage a closed incident).

Student B: Ahsan Abbas (53143) - Integration & Response Engineer

Working with the external APIs proved why patterns like Adapter and Proxy are mandatory in the real world. Outside systems are messy; the Adapter gave us a clean, unified format. Without the Proxy caching results, we would have hit external API rate limits immediately. The Decorator pattern was the most satisfying to build, as it allowed me to seamlessly wrap Audit Logging and Approval Gates around the core response logic without breaking the base action code.

Student C: Shaheer Fasil (53038) - SOC Platform Engineer

Implementing the Observer pattern via a real message broker (RabbitMQ) showed me the true power of Event-Driven Architecture. Because the backend services do not talk directly to the frontend, the dashboard updates seamlessly only when relevant events happen. Following the strict MVC structure for the dashboard kept the user interface React components completely separate from the complex backend state management, making the application much easier to maintain.

8. Conclusion

SDA-Pro successfully meets all requirements of the project charter. It ingests heterogeneous alerts, passes them down a structured enrichment chain, checks external databases safely via proxies, uses strategic playbooks to respond, and displays everything on a real-time MVC dashboard. The strict adherence to the 12 design patterns and 4 architectural styles ensured our codebase remained clean, modular, and highly extensible for future security operations.