package com.sdapro.alertingestion.services.normalizer;

import com.sdapro.alertingestion.domain.alert.CanonicalAlert;
import com.sdapro.alertingestion.domain.alert.Severity;
import com.sdapro.alertingestion.domain.source.AlertSourceType;

// PATTERN: Factory Method (Concrete Product)
// RATIONALE: Converts Splunk-specific alert format into CanonicalAlert
public class SplunkNormalizer implements AlertNormalizer {

    @Override
    public CanonicalAlert normalize(String rawPayload) {
        CanonicalAlert alert = new CanonicalAlert();
        alert.setSourceType("SPLUNK");
        alert.setRawPayload(rawPayload);
        alert.setSourceIp(extractField(rawPayload, "src_ip", "0.0.0.0"));
        alert.setDestinationIp(extractField(rawPayload, "dest_ip", "0.0.0.0"));
        alert.setEventType(extractField(rawPayload, "event_type", "UNKNOWN"));
        alert.setDescription(extractField(rawPayload, "message", "Splunk Alert"));
        alert.setSeverity(mapSeverity(extractField(rawPayload, "severity", "low")));
        return alert;
    }

    @Override
    public boolean supports(AlertSourceType sourceType) {
        return AlertSourceType.SPLUNK.equals(sourceType);
    }

    private String extractField(String payload, String field, String defaultValue) {
        String searchKey = field + "=";
        int start = payload.indexOf(searchKey);
        if (start == -1) return defaultValue;
        start += searchKey.length();
        int end = payload.indexOf(",", start);
        if (end == -1) end = payload.length();
        return payload.substring(start, end).trim();
    }

    private Severity mapSeverity(String splunkSeverity) {
        return switch (splunkSeverity.toLowerCase()) {
            case "critical" -> Severity.CRITICAL;
            case "high"     -> Severity.HIGH;
            case "medium"   -> Severity.MEDIUM;
            default         -> Severity.LOW;
        };
    }
}
