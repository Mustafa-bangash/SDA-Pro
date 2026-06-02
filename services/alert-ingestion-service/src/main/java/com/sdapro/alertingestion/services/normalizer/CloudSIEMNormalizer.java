package com.sdapro.alertingestion.services.normalizer;

import com.sdapro.alertingestion.domain.alert.CanonicalAlert;
import com.sdapro.alertingestion.domain.alert.Severity;
import com.sdapro.alertingestion.domain.source.AlertSourceType;

import java.time.Instant;
import java.util.UUID;

// PATTERN: Factory Method
// Concrete Product — created by AlertNormalizerFactory when source = CLOUD_SIEM
// Normalizes generic Cloud SIEM (Azure Sentinel / AWS GuardDuty style) JSON format
// Cloud SIEM format example:
// "sourceIp=10.0.0.1,destIp=192.168.1.1,riskScore=85,category=InitialAccess,description=Suspicious login"
public class CloudSIEMNormalizer implements AlertNormalizer {

    // PATTERN: Factory Method — supports() used by factory to select correct normalizer
    @Override
    public boolean supports(AlertSourceType sourceType) {
        return sourceType == AlertSourceType.CLOUD_SIEM;
    }

    @Override
    public CanonicalAlert normalize(String rawPayload) {
        if (rawPayload == null || rawPayload.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "CloudSIEMNormalizer: rawPayload cannot be null or empty");
        }

        // Parse key=value cloud SIEM format
        String sourceIp = extractField(rawPayload, "sourceIp", "0.0.0.0");
        String destIp = extractField(rawPayload, "destIp", "0.0.0.0");
        String riskScoreStr = extractField(rawPayload, "riskScore", "0");
        String category = extractField(rawPayload, "category", "UNKNOWN");
        String description = extractField(rawPayload, "description", "No description");

        int riskScore = 0;
        try { riskScore = Integer.parseInt(riskScoreStr); } catch (NumberFormatException ignored) {}

        // Map risk score to canonical severity
        Severity severity = mapToSeverity(riskScore);

        // Map MITRE ATT&CK category to canonical event type
        String eventType = mapToEventType(category);

        String summary = "CloudSIEM [" + category + "] from " + sourceIp
                + " to " + destIp + " riskScore=" + riskScore
                + " — " + description;

        CanonicalAlert alert = new CanonicalAlert();
        alert.setId(UUID.randomUUID());
        alert.setSourceType("CLOUD_SIEM");
        alert.setSeverity(severity);
        alert.setSourceIp(sourceIp);
        alert.setDestinationIp(destIp);
        alert.setEventType(eventType);
        alert.setSummary(summary);
        alert.setRawPayload(rawPayload);
        alert.setDetectedAt(Instant.now());

        return alert;
    }

    // Maps Cloud SIEM numeric risk score (0-100) to canonical severity
    private Severity mapToSeverity(int riskScore) {
        if (riskScore >= 90) return Severity.CRITICAL;
        if (riskScore >= 70) return Severity.HIGH;
        if (riskScore >= 40) return Severity.MEDIUM;
        return Severity.LOW;
    }

    // Maps MITRE ATT&CK category to canonical event type
    private String mapToEventType(String category) {
        switch (category.toUpperCase()) {
            case "INITIALACCESS":    return "INITIAL_ACCESS";
            case "EXECUTION":        return "MALWARE_EXECUTION";
            case "PERSISTENCE":      return "PERSISTENCE_ATTEMPT";
            case "PRIVILEGEESC":     return "PRIVILEGE_ESCALATION";
            case "LATERALMOVEMENT":  return "LATERAL_MOVEMENT_ATTEMPT";
            case "EXFILTRATION":     return "DATA_EXFILTRATION";
            case "COMMANDCONTROL":   return "C2_COMMUNICATION";
            default:                 return "CLOUD_SIEM_ALERT";
        }
    }

    private String extractField(String payload, String fieldName, String defaultValue) {
        String[] parts = payload.split(",");
        for (String part : parts) {
            String[] kv = part.trim().split("=", 2);
            if (kv.length == 2 && kv[0].trim().equalsIgnoreCase(fieldName)) {
                return kv[1].trim();
            }
        }
        return defaultValue;
    }
}
