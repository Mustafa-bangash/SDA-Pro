# SDA-Pro — Security Incident Response & Threat Mitigation Platform

## Course
Software Design & Architecture — Semester Project

## Project Overview
SDA-Pro is a distributed Security Operations Center (SOC) platform that ingests threat alerts
from multiple sources, enriches and classifies them through an intelligent pipeline, orchestrates
automated incident response actions, and provides real-time situational awareness to SOC analysts.

## Team Structure
| Role | Student | Patterns |
|------|---------|---------|
| Threat Pipeline Engineer | Student A | Singleton, Composite, Factory Method, Chain of Responsibility, Abstract Factory, Strategy, State |
| Integration & Response Engineer | Student B | Facade, Adapter, Decorator, Proxy, Chain of Responsibility |
| SOC Platform Engineer | Student C | MVC, Observer, SOA, Event-Driven, Layered Architecture |

## Tech Stack
- Backend: Java 17 Spring Boot 3.2
- Frontend: React
- Message Broker: RabbitMQ
- Database: PostgreSQL + Redis
- Containerization: Docker + Docker Compose

## Services
| Service | Port | Owner |
|---------|------|-------|
| alert-ingestion-service | 8081 | Student A |
| enrichment-correlation-service | 8082 | Student A |
| incident-management-service | 8083 | Student A |
| response-orchestration-service | 8084 | Student B |
| threat-intel-service | 8085 | Student B |
| notification-service | 8086 | Student B and C |
| audit-service | 8087 | Student C |
| soc-dashboard | 3000 | Student C |

## Design Patterns
| Pattern | Location |
|---------|----------|
| Singleton | IngestionConfigManager |
| Factory Method | AlertNormalizerFactory |
| Abstract Factory | EnrichmentProviderFactory |
| Composite | AlertComponent tree |
| Chain of Responsibility | EnrichmentPipeline |
| Strategy | CorrelationStrategySelector |
| State | IncidentContext |
| Facade | IncidentResponseFacade |
| Adapter | VirusTotalAdapter, MISPAdapter |
| Decorator | AuditLogDecorator, ApprovalGateDecorator |
| Proxy | ThreatIntelProxy |
| Observer | EventBusPublisher |

## How to Run
docker-compose up

## Git Branches
- main: production releases
- develop: integration branch
- feature/A-composite-state: Student A work
- feature/B-threat-intel-proxy: Student B work
- feature/C-dashboard-observer: Student C work
