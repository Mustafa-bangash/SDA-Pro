package com.sdapro.alertingestion.services.normalizer;

import com.sdapro.alertingestion.domain.alert.CanonicalAlert;
import com.sdapro.alertingestion.domain.alert.Severity;
import com.sdapro.alertingestion.domain.source.AlertSourceType;

// PATTERN: Factory Method (Concrete Product)
// RATIONALE: Converts CrowdStrike EDR-specific format into CanonicalAlert
public class CrowdStrikeNormalizer implements AlertNormalizer {

    @Override
    public CanonicalAlert normalize(String rawPayload) {
        CanonicalAlert alert = new CanonicalAlert();
        alert.setSourceType("CROWDSTRIKE");
        alert.setRawPayload(rawPayload);
        alert.setSourceIp(extractField(rawPayload, "device_ip", "0.0.0.0"));
        alert.setDestinationIp(extractField(rawPayload, "target_ip", "0.0.0.0"));
        alert.setEventType(extractField(rawPayload, "detection_type", "UNKNOWN"));
        alert.setDescription(extractField(rawPayload, "description", "CrowdStrike Alert"));
        alert.setSeverity(mapSeverity(extractField(rawPayload, "severity_level", "20")));
        return alert;
    }

    @Override
    public boolean supports(AlertSourceType sourceType) {
        return AlertSourceType.CROWDSTRIKE.equals(sourceType);
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

    private Severity mapSeverity(String severityLevel) {
        try {
            int level = Integer.parseInt(severityLevel);
            if (level >= 80) return Severity.CRITICAL;
            if (level >= 60) return Severity.HIGH;
            if (level >= 40) return Severity.MEDIUM;
            return Severity.LOW;
        } catch (NumberFormatException e) {
            return Severity.LOW;
        }
    }
}
