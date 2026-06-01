# SDA-Pro — Makefile
# Usage: make <target>

.PHONY: up down build test clean logs help

# ─────────────────────────────────────────
# DOCKER TARGETS
# ─────────────────────────────────────────

up:
	@echo "Starting SDA-Pro full stack..."
	docker-compose up --build -d
	@echo "Services started:"
	@echo "  alert-ingestion-service  -> http://localhost:8081/api/v1/alerts/health"
	@echo "  enrichment-service       -> http://localhost:8082/api/v1/enrichment/health"
	@echo "  incident-service         -> http://localhost:8083/api/v1/incidents/health"
	@echo "  RabbitMQ Management      -> http://localhost:15672 (sdapro/sdapro123)"

down:
	@echo "Stopping SDA-Pro stack..."
	docker-compose down

build:
	@echo "Building all service images..."
	docker-compose build --no-cache

logs:
	docker-compose logs -f

logs-ingestion:
	docker-compose logs -f alert-ingestion-service

logs-enrichment:
	docker-compose logs -f enrichment-correlation-service

logs-incident:
	docker-compose logs -f incident-management-service

clean:
	@echo "Stopping containers and removing volumes..."
	docker-compose down -v
	@echo "Removing dangling images..."
	docker image prune -f

# ─────────────────────────────────────────
# HEALTH CHECK TARGETS
# ─────────────────────────────────────────

health:
	@echo "Checking service health..."
	@curl -s http://localhost:8081/api/v1/alerts/health && echo " [alert-ingestion OK]" || echo " [alert-ingestion FAILED]"
	@curl -s http://localhost:8082/api/v1/enrichment/health && echo " [enrichment OK]" || echo " [enrichment FAILED]"
	@curl -s http://localhost:8083/api/v1/incidents/health && echo " [incident OK]" || echo " [incident FAILED]"

# ─────────────────────────────────────────
# TEST TARGETS
# ─────────────────────────────────────────

test:
	@echo "Running unit tests for all Student A services..."
	cd services/alert-ingestion-service && mvn test -q
	cd services/enrichment-correlation-service && mvn test -q
	cd services/incident-management-service && mvn test -q
	@echo "All tests passed!"

test-ingestion:
	cd services/alert-ingestion-service && mvn test -q

test-enrichment:
	cd services/enrichment-correlation-service && mvn test -q

test-incident:
	cd services/incident-management-service && mvn test -q

# ─────────────────────────────────────────
# DEMO TARGETS
# ─────────────────────────────────────────

demo-ingest:
	@echo "Sending demo Splunk alert..."
	curl -s -X POST http://localhost:8081/api/v1/alerts/ingest/splunk \
		-H "Content-Type: application/json" \
		-d '{"data": "src_ip=192.168.1.100,dest_ip=10.0.0.5,severity=critical,event_type=RANSOMWARE,message=Ransomware detected on endpoint"}' \
		| python3 -m json.tool

demo-enrich:
	@echo "Running enrichment pipeline..."
	curl -s -X POST http://localhost:8082/api/v1/enrichment/enrich \
		-H "Content-Type: application/json" \
		-d '{"alertData": "src_ip=192.168.1.100,malicious=true,server=prod-db-01"}' \
		| python3 -m json.tool

demo-incident:
	@echo "Creating demo incident..."
	curl -s -X POST http://localhost:8083/api/v1/incidents \
		-H "Content-Type: application/json" \
		-d '{"severity": "CRITICAL", "source": "enrichment-service"}' \
		| python3 -m json.tool

demo-full:
	@echo "Running full end-to-end demo flow..."
	@make demo-ingest
	@sleep 1
	@make demo-enrich
	@sleep 1
	@make demo-incident
	@echo "End-to-end demo complete!"

# ─────────────────────────────────────────
# GIT TARGETS
# ─────────────────────────────────────────

git-status:
	git log --oneline -10
	git branch -a

tag:
	@echo "Current tags:"
	git tag -l

# ─────────────────────────────────────────
# HELP
# ─────────────────────────────────────────

help:
	@echo "SDA-Pro Makefile Commands:"
	@echo ""
	@echo "  Docker:"
	@echo "    make up              Start full stack with docker-compose"
	@echo "    make down            Stop all containers"
	@echo "    make build           Rebuild all images"
	@echo "    make logs            Follow all container logs"
	@echo "    make clean           Stop + remove volumes + prune images"
	@echo ""
	@echo "  Health:"
	@echo "    make health          Check all service health endpoints"
	@echo ""
	@echo "  Testing:"
	@echo "    make test            Run all unit tests"
	@echo "    make test-ingestion  Run alert-ingestion tests only"
	@echo "    make test-enrichment Run enrichment tests only"
	@echo "    make test-incident   Run incident tests only"
	@echo ""
	@echo "  Demo:"
	@echo "    make demo-ingest     Send demo Splunk alert"
	@echo "    make demo-enrich     Run enrichment pipeline"
	@echo "    make demo-incident   Create demo incident"
	@echo "    make demo-full       Run full end-to-end flow"
