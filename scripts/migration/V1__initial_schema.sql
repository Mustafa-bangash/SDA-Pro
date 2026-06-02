-- SDA-Pro Initial Schema Migration
-- V1: Create core tables for incidents, alerts, and audit logs

CREATE TABLE IF NOT EXISTS incidents (
    id UUID PRIMARY KEY,
    current_state VARCHAR(50) NOT NULL DEFAULT 'NEW',
    severity VARCHAR(20) NOT NULL,
    triggering_alert_id UUID,
    assigned_analyst VARCHAR(100),
    state_history TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS canonical_alerts (
    id UUID PRIMARY KEY,
    source_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    source_ip VARCHAR(45),
    destination_ip VARCHAR(45),
    event_type VARCHAR(100),
    summary TEXT,
    raw_payload TEXT,
    detected_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    event_data TEXT,
    occurred_at TIMESTAMP NOT NULL DEFAULT NOW(),
    immutable BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_incidents_state ON incidents(current_state);
CREATE INDEX idx_alerts_severity ON canonical_alerts(severity);
CREATE INDEX idx_audit_event_type ON audit_logs(event_type);
