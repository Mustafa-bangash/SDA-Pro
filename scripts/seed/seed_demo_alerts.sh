#!/bin/bash
# SDA-Pro Demo Seed Script
# Sends demo alerts to all 3 ingestion endpoints
# Usage: ./seed_demo_alerts.sh

BASE_URL="http://localhost:8081/api/v1/alerts"

echo "=========================================="
echo "SDA-Pro Demo Alert Seeder"
echo "=========================================="

# Check service is running
echo "Checking alert-ingestion-service health..."
health=$(curl -s $BASE_URL/health)
if [ "$health" != "alert-ingestion-service is running" ]; then
    echo "ERROR: alert-ingestion-service is not running!"
    echo "Run 'make up' first then retry."
    exit 1
fi
echo "Service is healthy. Starting seed..."
echo ""

# Seed Splunk alerts
echo "--- Sending Splunk alerts ---"
curl -s -X POST $BASE_URL/ingest/splunk \
    -H "Content-Type: application/json" \
    -d '{"data": "src_ip=192.168.1.100,dest_ip=10.0.0.5,severity=critical,event_type=RANSOMWARE,message=Ransomware detected on endpoint prod-server-01"}' \
    | python3 -m json.tool
sleep 1

curl -s -X POST $BASE_URL/ingest/splunk \
    -H "Content-Type: application/json" \
    -d '{"data": "src_ip=10.0.0.22,dest_ip=172.16.0.1,severity=high,event_type=LOGIN_FAILURE,message=Multiple failed login attempts detected"}' \
    | python3 -m json.tool
sleep 1

curl -s -X POST $BASE_URL/ingest/splunk \
    -H "Content-Type: application/json" \
    -d '{"data": "src_ip=192.168.1.55,dest_ip=10.0.0.8,severity=medium,event_type=PORT_SCAN,message=Port scan detected from internal host"}' \
    | python3 -m json.tool
sleep 1

echo ""
echo "--- Sending CrowdStrike alerts ---"
curl -s -X POST $BASE_URL/ingest/crowdstrike \
    -H "Content-Type: application/json" \
    -d '{"data": "device_ip=10.0.0.15,target_ip=192.168.1.200,severity_level=95,detection_type=MALWARE,description=Ransomware binary executed on endpoint"}' \
    | python3 -m json.tool
sleep 1

curl -s -X POST $BASE_URL/ingest/crowdstrike \
    -H "Content-Type: application/json" \
    -d '{"data": "device_ip=10.0.0.30,target_ip=192.168.1.100,severity_level=75,detection_type=SUSPICIOUS_PROCESS,description=Suspicious PowerShell execution detected"}' \
    | python3 -m json.tool
sleep 1

echo ""
echo "--- Sending Firewall alerts ---"
curl -s -X POST $BASE_URL/ingest/firewall \
    -H "Content-Type: application/json" \
    -d '{"data": "action=DENY,src_ip=192.168.1.1,dst_ip=10.0.0.5,dst_port=445,protocol=TCP,rule=BLOCK_SMB,bytes=1024"}' \
    | python3 -m json.tool
sleep 1

curl -s -X POST $BASE_URL/ingest/firewall \
    -H "Content-Type: application/json" \
    -d '{"data": "action=DENY,src_ip=10.0.0.22,dst_ip=192.168.1.50,dst_port=3389,protocol=TCP,rule=BLOCK_RDP,bytes=512"}' \
    | python3 -m json.tool
sleep 1

echo ""
echo "--- Verifying all alerts ingested ---"
echo "Total alerts stored:"
curl -s $BASE_URL | python3 -m json.tool

echo ""
echo "=========================================="
echo "Seed complete! 8 alerts ingested."
echo "Next: Run enrichment pipeline on each alert"
echo "=========================================="
