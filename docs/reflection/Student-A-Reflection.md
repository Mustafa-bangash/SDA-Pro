# Individual Reflection Report — Student A
## SDA-Pro: Security Incident Response & Threat Mitigation Platform
### Software Design & Architecture — Semester Project

---

## 1. My Role & Responsibilities

As the **Threat Pipeline Engineer (Student A)**, I was responsible for designing
and implementing the core alert processing pipeline of SDA-Pro. My ownership covered
three microservices:

- **alert-ingestion-service** (port 8081) — Singleton, Factory Method, Composite
- **enrichment-correlation-service** (port 8082) — Chain of Responsibility, Abstract Factory, Strategy
- **incident-management-service** (port 8083) — State

I also contributed to the shared/ library (domain event schemas, contracts, commons),
all 5 Architecture Decision Records (ADRs), all 4 PlantUML UML diagrams, the
docker-compose.yml, Makefile, OpenAPI contracts, seed scripts, and the end-to-end
integration test suite.

---

## 2. Design Patterns — Decisions and Justifications

### 2.1 Singleton — IngestionConfigManager

**Decision:** I implemented IngestionConfigManager as a Singleton to provide a
single shared configuration source across the alert-ingestion-service.

**Justification:** Alert ingestion configuration (enabled sources, batch sizes,
polling intervals) must be consistent across all components of the service. If
multiple instances of the config manager existed, one component could see Splunk
as enabled while another sees it disabled, causing unpredictable behavior during
high-volume ingestion. The Singleton guarantees a single source of truth.

**What I learned:** Singleton is often misused as a global variable. The key
justification here is not convenience but consistency — the config must be the
same everywhere in the service at all times. I also learned that Singleton
requires careful thread-safety consideration in production (double-checked locking
or enum-based Singleton for Java).

**Honest reflection:** If I were to rebuild this, I would consider using Spring's
built-in `@Configuration` and `@Bean` with singleton scope instead of manually
implementing the Singleton pattern, as Spring already guarantees single instance
per application context. However, manually implementing it demonstrates clear
understanding of the pattern for this course.

---

### 2.2 Factory Method — AlertNormalizerFactory

**Decision:** I used the Factory Method pattern in AlertNormalizerFactory to
create the correct AlertNormalizer (Splunk, CrowdStrike, Firewall) based on the
alert source type at runtime.

**Justification:** SDA-Pro ingests alerts from heterogeneous sources — Splunk
uses key=value format, CrowdStrike uses numeric severity levels, Firewall uses
syslog format. Without Factory Method, the ingestion controller would need a
large if-else or switch statement that violates the Open/Closed Principle. With
Factory Method, adding a new source (e.g., Microsoft Sentinel) requires only
creating a new normalizer class and registering it — no existing code changes.

**What I learned:** The Factory Method pattern is most valuable when object
creation logic is complex or varies by type. The register() method I added to
the factory makes it truly open for extension — this is the practical benefit
of the pattern beyond just hiding constructors.

**Honest reflection:** I initially confused Factory Method with Abstract Factory.
Factory Method creates one product (a normalizer). Abstract Factory creates
families of related products (GeoIP provider + ThreatIntel provider + Asset
provider together). Understanding this distinction through implementation was
the most valuable learning moment of the project.

---

### 2.3 Abstract Factory — EnrichmentProviderFactory

**Decision:** I used Abstract Factory to create families of enrichment providers.
PremiumEnrichmentFactory creates premium-tier GeoIP, ThreatIntel, and Asset
providers together. StandardEnrichmentFactory creates standard-tier versions.

**Justification:** Enrichment providers must be compatible within a family. A
premium GeoIP service returns city-level location data that a premium threat
intel service can cross-reference. Mixing premium GeoIP with standard threat
intel would produce incompatible enrichment results. Abstract Factory enforces
family consistency at the factory level.

**What I learned:** Abstract Factory solves the problem of family compatibility
that Factory Method alone cannot. The factory selection (premium vs standard)
happens once at service startup based on license configuration, and all
downstream components receive compatible provider instances automatically.

---

### 2.4 Composite — Alert, AlertCampaign, IncidentCluster

**Decision:** I modeled the alert hierarchy as a Composite tree where SingleAlert
is the leaf node, and AlertCampaign and IncidentCluster are composite nodes that
contain other AlertComponents.

**Justification:** Modern cyberattacks are multi-stage campaigns, not isolated
events. A ransomware attack might involve 50 individual alerts (port scans,
lateral movement, credential dumps, encryption activity) that belong to one
campaign. Without Composite, the enrichment pipeline would need separate code
paths for individual alerts vs campaigns. With Composite, the pipeline calls
enrich(alertComponent) uniformly regardless of whether it receives a single
alert or a campaign of 50 alerts.

**What I learned:** The power of Composite is in uniform treatment. The
ClassificationHandler does not know or care whether it is classifying one alert
or a campaign — it calls getSeverity() and getChildren() on the interface. This
eliminated a significant amount of conditional logic from the enrichment pipeline.

**Honest reflection:** The hardest part was deciding what operations belong on
the AlertComponent interface. I initially put too many methods there, violating
interface segregation. I refined it to only getId(), getSeverity(), getTimestamp(),
add(), remove(), and getChildren() — the minimum needed for uniform treatment.

---

### 2.5 Chain of Responsibility — Enrichment Pipeline

**Decision:** I implemented the enrichment pipeline as a Chain of Responsibility
with five handlers: DeduplicationHandler → GeoIPHandler → ThreatIntelHandler →
AssetContextHandler → ClassificationHandler.

**Justification:** Each enrichment stage has a single responsibility and should
not know about other stages. DeduplicationHandler should not know that GeoIP
enrichment happens next. This allows the pipeline to be reconfigured dynamically
— in a high-load scenario, the AssetContextHandler can be removed from the chain
without modifying any other handler. The pipeline assembler (EnrichmentPipelineAssembler)
constructs the chain independently of the handlers themselves.

**What I learned:** Chain of Responsibility is most powerful when combined with
the ability to stop the chain early. My DeduplicationHandler stops the chain
immediately for duplicate alerts, preventing unnecessary GeoIP API calls and
threat intel lookups — this has a direct performance benefit. I learned that
the pattern is not just about passing requests along but also about the ability
to short-circuit processing.

---

### 2.6 Strategy — CorrelationStrategySelector

**Decision:** I implemented three correlation strategies: AggressiveCorrelationStrategy
(creates incident for any non-low severity), BalancedCorrelationStrategy (considers
threat score and asset criticality together), and ConservativeCorrelationStrategy
(only creates incident for confirmed critical malicious alerts).

**Justification:** Different SOC environments have different risk tolerances. A
financial institution running a 24/7 SOC may want aggressive correlation to catch
every potential threat. A small business with limited analyst capacity needs
conservative correlation to avoid alert fatigue. Strategy allows the algorithm
to be swapped without changing the enrichment service's core logic.

**What I learned:** Strategy pattern requires a clear common interface that all
strategies implement. Defining CorrelationStrategy.correlate(alertData, enrichmentResult)
as the interface forced me to think carefully about what inputs all strategies
need and what output they all produce. This upfront interface design is the most
important step in implementing Strategy correctly.

---

### 2.7 State — Incident Lifecycle

**Decision:** I modeled the incident lifecycle as a State machine with seven
states: New → UnderTriage → Containment → Eradication → Recovery →
PostIncidentReview → Closed.

**Justification:** Incident behavior is fundamentally state-dependent. A CLOSED
incident must reject all transition requests. A NEW incident must reject
containment requests (cannot skip triage). Without State pattern, the Incident
class would have massive if-else blocks checking current state before every
operation. With State, each state class encapsulates its own legal transitions
and throws IllegalStateException for invalid ones. Adding a new state (e.g.,
ESCALATED) requires only a new state class, not modifying existing states.

**What I learned:** The State pattern eliminates state-checking conditional logic
by moving it into the state objects themselves. The most important insight was
that state objects should be stateless — they receive the context (IncidentContext)
as a parameter and modify it, rather than holding state themselves. This keeps
state objects lightweight and reusable.

**Honest reflection:** The trickiest part was defining exactly which transitions
are legal from each state. I used the NIST incident response framework
(Preparation → Detection → Containment → Eradication → Recovery → Post-Incident)
as the real-world justification for the transition rules, which made the design
decisions defensible.

---

## 3. Architecture Decisions

### SOA over Microservices (ADR-001)

The most important architectural decision was choosing SOA over pure microservices.
With a 3-person team and a 2-week timeline, microservices would have introduced
too much operational overhead (service mesh, distributed tracing, complex
networking). SOA gave us autonomous services with clear boundaries while keeping
the team productive.

### Hybrid Communication (ADR-002)

Using REST for synchronous request-response (dashboard queries incidents) and
RabbitMQ for asynchronous events (alert ingested, incident state changed) was
the right decision. The enrichment pipeline is non-blocking because alert
ingestion publishes an event and continues — it does not wait for enrichment
to finish. This was a direct architectural benefit of the hybrid approach.

---

## 4. Challenges Faced

**Challenge 1 — Pattern confusion early on:** I initially confused Factory Method
with Simple Factory, and Abstract Factory with Factory Method. Working through
the EnrichmentProviderFactory implementation clarified the distinction: Factory
Method creates one product, Abstract Factory creates a family.

**Challenge 2 — Composite interface design:** Deciding what belongs on the
AlertComponent interface was difficult. Leaf nodes (SingleAlert) cannot have
children, so add() and remove() must throw UnsupportedOperationException. This
is a known limitation of Composite and required careful thought.

**Challenge 3 — State transition rules:** Defining legal transitions required
real-world knowledge of incident response frameworks. Consulting NIST SP 800-61
gave the transitions real justification beyond just pattern mechanics.

---

## 5. What I Would Do Differently

1. **Define the shared/ canonical event schemas first** before writing any
   service code. We defined them later, which required some refactoring.

2. **Write unit tests before implementation** (TDD approach). Writing tests
   after the fact was harder because some classes were not designed with
   testability in mind.

3. **Use Spring's dependency injection** for Singleton management rather than
   manual getInstance() implementation in a real production system.

---

## 6. Key Takeaways

- Design patterns are solutions to recurring design problems, not goals in themselves.
  Every pattern in this project was justified by a concrete requirement, not added
  for the sake of using patterns.

- The Chain of Responsibility and Strategy patterns together make the enrichment
  pipeline highly extensible — new handlers and new correlation algorithms can be
  added without touching existing code.

- The State pattern eliminated more conditional logic than any other pattern in
  the project. The IncidentContext class went from a potential 200-line if-else
  monster to a clean 30-line class that delegates everything to state objects.

- SOA forces you to think about service boundaries and communication contracts
  upfront. The shared/ library and OpenAPI contracts were the most important
  artifacts for team coordination.

---

*Student A — Threat Pipeline Engineer*
*Software Design & Architecture — 6th Semester*
