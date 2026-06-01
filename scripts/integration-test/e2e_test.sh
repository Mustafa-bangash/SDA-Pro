#!/bin/bash
# SDA-Pro End-to-End Integration Test
# Tests the full flow: Ingest -> Enrich -> Correlate -> Create Incident -> State Transitions
# Usage: ./e2e_test.sh

INGESTION_URL="http://localhost:8081/api/v1/alerts"
ENRICHMENT_URL="http://localhost:8082/api/v1/enrichment"
INCIDENT_URL="http://localhost:8083/api/v1/incidents"

PASS=0
FAIL=0

echo "=========================================="
echo "SDA-Pro End-to-End Integration Test Suite"
echo "=========================================="
echo ""

# Helper functions
pass() { echo "  PASS: $1"; PASS=$((PASS+1)); }
fail() { echo "  FAIL: $1"; FAIL=$((FAIL+1)); }
section() { echo ""; echo "--- $1 ---"; }

# ─────────────────────────────────────────
# TEST 1: Health Checks
# ─────────────────────────────────────────
section "TEST 1: Service Health Checks"

health1=$(curl -s $INGESTION_URL/health)
if [ "$health1" = "alert-ingestion-service is running" ]; then
    pass "alert-ingestion-service is healthy"
else
    fail "alert-ingestion-service is NOT healthy: $health1"
fi

health2=$(curl -s $ENRICHMENT_URL/health)
if [ "$health2" = "enrichment-correlation-service is running" ]; then
    pass "enrichment-correlation-service is healthy"
else
    fail "enrichment-correlation-service is NOT healthy: $health2"
fi

health3=$(curl -s $INCIDENT_URL/health)
if [ "$health3" = "incident-management-service is running" ]; then
    pass "incident-management-service is healthy"
else
    fail "incident-management-service is NOT healthy: $health3"
fi

# ─────────────────────────────────────────
# TEST 2: Alert Ingestion - Factory Method
# ─────────────────────────────────────────
section "TEST 2: Alert Ingestion (Factory Method + Composite)"

# Ingest Splunk alert
splunk_response=$(curl -s -X POST $INGESTION_URL/ingest/splunk \
    -H "Content-Type: application/json" \
    -d '{"data": "src_ip=192.168.1.100,dest_ip=10.0.0.5,severity=critical,event_type=RANSOMWARE,message=Ransomware detected"}')

if echo "$splunk_response" | grep -q "SPLUNK"; then
    pass "Splunk alert ingested and normalized correctly"
else
    fail "Splunk alert ingestion failed: $splunk_response"
fi

# Ingest CrowdStrike alert
crowdstrike_response=$(curl -s -X POST $INGESTION_URL/ingest/crowdstrike \
    -H "Content-Type: application/json" \
    -d '{"data": "device_ip=10.0.0.15,target_ip=192.168.1.200,severity_level=95,detection_type=MALWARE,description=Ransomware binary executed"}')

if echo "$crowdstrike_response" | grep -q "CROWDSTRIKE"; then
    pass "CrowdStrike alert ingested and normalized correctly"
else
    fail "CrowdStrike alert ingestion failed: $crowdstrike_response"
fi

# Ingest Firewall alert
firewall_response=$(curl -s -X POST $INGESTION_URL/ingest/firewall \
    -H "Content-Type: application/json" \
    -d '{"data": "action=DENY,src_ip=192.168.1.1,dst_ip=10.0.0.5,dst_port=445,protocol=TCP,rule=BLOCK_SMB,bytes=1024"}')

if echo "$firewall_response" | grep -q "FIREWALL"; then
    pass "Firewall alert ingested and normalized correctly"
else
    fail "Firewall alert ingestion failed: $firewall_response"
fi

# Verify all alerts stored
all_alerts=$(curl -s $INGESTION_URL)
alert_count=$(echo "$all_alerts" | python3 -c "import sys,json; print(len(json.load(sys.stdin)))" 2>/dev/null)
if [ "$alert_count" -ge "3" ]; then
    pass "All $alert_count alerts stored correctly"
else
    fail "Expected 3+ alerts, got: $alert_count"
fi

# ─────────────────────────────────────────
# TEST 3: Enrichment Pipeline - Chain of Responsibility
# ─────────────────────────────────────────
section "TEST 3: Enrichment Pipeline (Chain of Responsibility + Strategy)"

enrich_response=$(curl -s -X POST $ENRICHMENT_URL/enrich \
    -H "Content-Type: application/json" \
    -d '{"alertData": "src_ip=192.168.1.100,malicious=true,server=prod-db-01"}')

if echo "$enrich_response" | grep -q "COMPLETE"; then
    pass "Enrichment pipeline completed successfully"
else
    fail "Enrichment pipeline failed: $enrich_response"
fi

if echo "$enrich_response" | grep -q "geoLocation"; then
    pass "GeoIP enrichment handler executed"
else
    fail "GeoIP handler did not enrich"
fi

if echo "$enrich_response" | grep -q "threatIntelScore"; then
    pass "ThreatIntel enrichment handler executed"
else
    fail "ThreatIntel handler did not enrich"
fi

if echo "$enrich_response" | grep -q "classification"; then
    pass "Classification handler executed"
else
    fail "Classification handler did not execute"
fi

# ─────────────────────────────────────────
# TEST 4: Correlation Strategy
# ─────────────────────────────────────────
section "TEST 4: Correlation Strategy (Strategy Pattern)"

correlate_response=$(curl -s -X POST $ENRICHMENT_URL/correlate \
    -H "Content-Type: application/json" \
    -d '{"alertData": "src_ip=192.168.1.100,malicious=true,server=prod-db-01"}')

if echo "$correlate_response" | grep -q "recommendedAction"; then
    pass "Correlation strategy executed and returned action"
else
    fail "Correlation strategy failed: $correlate_response"
fi

# ─────────────────────────────────────────
# TEST 5: Incident Lifecycle - State Pattern
# ─────────────────────────────────────────
section "TEST 5: Incident Lifecycle (State Pattern)"

# Create incident
create_response=$(curl -s -X POST $INCIDENT_URL \
    -H "Content-Type: application/json" \
    -d '{"severity": "CRITICAL", "source": "e2e-test"}')

incident_id=$(echo "$create_response" | python3 -c "import sys,json; print(json.load(sys.stdin)['incidentId'])" 2>/dev/null)

if [ -n "$incident_id" ]; then
    pass "Incident created with ID: $incident_id"
else
    fail "Incident creation failed: $create_response"
    exit 1
fi

# Verify starts in NEW state
state=$(echo "$create_response" | python3 -c "import sys,json; print(json.load(sys.stdin)['state'])" 2>/dev/null)
if [ "$state" = "NEW" ]; then
    pass "Incident starts in NEW state"
else
    fail "Expected NEW state, got: $state"
fi

# Transition to UNDER_TRIAGE
triage_response=$(curl -s -X POST $INCIDENT_URL/$incident_id/triage \
    -H "Content-Type: application/json")
triage_state=$(echo "$triage_response" | python3 -c "import sys,json; print(json.load(sys.stdin)['newState'])" 2>/dev/null)
if [ "$triage_state" = "UNDER_TRIAGE" ]; then
    pass "Incident transitioned NEW -> UNDER_TRIAGE"
else
    fail "Triage transition failed: $triage_response"
fi

# Transition to CONTAINMENT
contain_response=$(curl -s -X POST $INCIDENT_URL/$incident_id/containment \
    -H "Content-Type: application/json")
contain_state=$(echo "$contain_response" | python3 -c "import sys,json; print(json.load(sys.stdin)['newState'])" 2>/dev/null)
if [ "$contain_state" = "CONTAINMENT" ]; then
    pass "Incident transitioned UNDER_TRIAGE -> CONTAINMENT"
else
    fail "Containment transition failed: $contain_response"
fi

# Transition to ERADICATION
erad_response=$(curl -s -X POST $INCIDENT_URL/$incident_id/eradication \
    -H "Content-Type: application/json")
erad_state=$(echo "$erad_response" | python3 -c "import sys,json; print(json.load(sys.stdin)['newState'])" 2>/dev/null)
if [ "$erad_state" = "ERADICATION" ]; then
    pass "Incident transitioned CONTAINMENT -> ERADICATION"
else
    fail "Eradication transition failed: $erad_response"
fi

# Transition to RECOVERY
recovery_response=$(curl -s -X POST $INCIDENT_URL/$incident_id/recovery \
    -H "Content-Type: application/json")
recovery_state=$(echo "$recovery_response" | python3 -c "import sys,json; print(json.load(sys.stdin)['newState'])" 2>/dev/null)
if [ "$recovery_state" = "RECOVERY" ]; then
    pass "Incident transitioned ERADICATION -> RECOVERY"
else
    fail "Recovery transition failed: $recovery_response"
fi

# Transition to POST_INCIDENT_REVIEW
review_response=$(curl -s -X POST $INCIDENT_URL/$incident_id/review \
    -H "Content-Type: application/json")
review_state=$(echo "$review_response" | python3 -c "import sys,json; print(json.load(sys.stdin)['newState'])" 2>/dev/null)
if [ "$review_state" = "POST_INCIDENT_REVIEW" ]; then
    pass "Incident transitioned RECOVERY -> POST_INCIDENT_REVIEW"
else
    fail "Review transition failed: $review_response"
fi

# Transition to CLOSED
close_response=$(curl -s -X POST $INCIDENT_URL/$incident_id/close \
    -H "Content-Type: application/json")
close_state=$(echo "$close_response" | python3 -c "import sys,json; print(json.load(sys.stdin)['newState'])" 2>/dev/null)
if [ "$close_state" = "CLOSED" ]; then
    pass "Incident transitioned POST_INCIDENT_REVIEW -> CLOSED"
else
    fail "Close transition failed: $close_response"
fi

# Test invalid transition - cannot reopen closed incident
invalid_response=$(curl -s -o /dev/null -w "%{http_code}" -X POST $INCIDENT_URL/$incident_id/triage \
    -H "Content-Type: application/json")
if [ "$invalid_response" = "400" ]; then
    pass "Invalid state transition correctly rejected with 400"
else
    fail "Expected 400 for invalid transition, got: $invalid_response"
fi

# ─────────────────────────────────────────
# TEST 6: Singleton Config
# ─────────────────────────────────────────
section "TEST 6: Singleton Config Endpoint"

config_response=$(curl -s $INGESTION_URL/config)
if echo "$config_response" | grep -q "maxBatchSize"; then
    pass "Singleton IngestionConfigManager config endpoint works"
else
    fail "Config endpoint failed: $config_response"
fi

# ─────────────────────────────────────────
# RESULTS
# ─────────────────────────────────────────
echo ""
echo "=========================================="
echo "Integration Test Results"
echo "=========================================="
echo "  PASSED: $PASS"
echo "  FAILED: $FAIL"
echo "  TOTAL:  $((PASS+FAIL))"
echo ""
if [ "$FAIL" = "0" ]; then
    echo "ALL TESTS PASSED - SDA-Pro is working end-to-end!"
else
    echo "SOME TESTS FAILED - Check service logs with: make logs"
fi
echo "=========================================="
