package com.sdapro.alertingestion.domain.source;

// PATTERN: Factory Method (used as key to select correct normalizer)
// RATIONALE: Each source type maps to a specific normalizer implementation
public enum AlertSourceType {
    SPLUNK,
    CROWDSTRIKE,
    FIREWALL,
    CLOUD_SIEM
}
