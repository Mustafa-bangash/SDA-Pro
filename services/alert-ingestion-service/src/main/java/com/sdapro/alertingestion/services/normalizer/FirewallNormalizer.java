package com.sdapro.alertingestion.services.normalizer;

import com.sdapro.alertingestion.domain.alert.CanonicalAlert;
import com.sdapro.alertingestion.domain.alert.Severity;
import com.sdapro.alertingestion.domain.source.AlertSourceType;

import java.time.Instant;
import java.util.UUID;

// PATTERN: Factory Method
// Concrete Product — created by AlertNormalizerFactory when source = FIREWALL
// Normalizes Palo Alto / generic firewall syslog format into CanonicalAlert
// Firewall syslog format example:
// "action=DENY,src_ip=192.168.1.1,dst_ip=10.0.0.5,dst_port=445,protocol=TCP,rule=BLOCK_SMB,bytes=1024"
public class FirewallNormalizer implements AlertNormalizer {

    // PATTERN: Factory Method — supports() used by factory to find correct normalizer
    @Override
    public boolean supports(AlertSourceType sourceType) {
        return sourceType == AlertSourceType.FIREWALL;
    }

    @Override
    public CanonicalAlert normalize(String rawPayload) {
        if (rawPayload == null || rawPayload.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "FirewallNormalizer: rawPayload cannot be null or empty");
        }

        // Parse key=value syslog format
        String srcIp = extractField(rawPayload, "src_ip", "0.0.0.0");
        String dstIp = extractField(rawPayload, "dst_ip", "0.0.0.0");
        String action = extractField(rawPayload, "action", "UNKNOWN");
        String rule = extractField(rawPayload, "rule", "UNKNOWN_RULE");
        String protocol = extractField(rawPayload, "protocol", "TCP");
        String dstPort = extractField(rawPayload, "dst_port", "0");

        // Map firewall action + rule to severity
        Severity severity = mapToSeverity(action, rule, dstPort);

        // Map firewall action to event type
        String eventType = mapToEventType(action, rule);

        // Build summary
        String summary = "Firewall " + action + " from " + srcIp +
                         " to " + dstIp + ":" + dstPort +
                         " via " + protocol + " rule=" + rule;

        // Build canonical alert
        CanonicalAlert alert = new CanonicalAlert();
        alert.setId(UUID.randomUUID());
        alert.setSourceType("FIREWALL");
        alert.setSeverity(severity);
        alert.setSourceIp(srcIp);
        alert.setDestinationIp(dstIp);
        alert.setEventType(eventType);
        alert.setSummary(summary);
        alert.setRawPayload(rawPayload);
        alert.setDetectedAt(Instant.now());

        return alert;
    }

    // Maps firewall action and rule to canonical severity
    private Severity mapToSeverity(String action, String rule, String dstPort) {
        int port = 0;
        try { port = Integer.parseInt(dstPort); } catch (NumberFormatException ignored) {}

        if (action.equalsIgnoreCase("DENY")) {
            if (port == 445 || port == 3389) return Severity.CRITICAL;
            if (port == 22) return Severity.HIGH;
            if (rule.contains("BLOCK") || rule.contains("THREAT")) return Severity.HIGH;
            return Severity.MEDIUM;
        }

        if (action.equalsIgnoreCase("ALLOW")) {
            if (port == 445 || port == 3389) return Severity.HIGH;
            if (rule.contains("SUSPICIOUS")) return Severity.MEDIUM;
            return Severity.LOW;
        }

        return Severity.LOW;
    }

    // Maps firewall action and rule to canonical event type
    private String mapToEventType(String action, String rule) {
        if (rule.contains("SMB") || rule.contains("445")) return "LATERAL_MOVEMENT_ATTEMPT";
        if (rule.contains("RDP") || rule.contains("3389")) return "REMOTE_ACCESS_ATTEMPT";
        if (rule.contains("THREAT") || rule.contains("MALWARE")) return "MALWARE_COMMUNICATION";
        if (rule.contains("SCAN") || rule.contains("PROBE")) return "PORT_SCAN";
        if (action.equalsIgnoreCase("DENY")) return "FIREWALL_BLOCK";
        return "FIREWALL_EVENT";
    }

    // Extracts value from "key=value,key2=value2" format
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
