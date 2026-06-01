package com.sdapro.shared.commons;

// Shared validation utility used across all services
// Validates canonical alert fields, IP addresses, incident IDs
public class ValidationUtils {

    // Validate IP address format
    public static boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty()) return false;
        String[] parts = ip.split("\\.");
        if (parts.length != 4) return false;
        for (String part : parts) {
            try {
                int val = Integer.parseInt(part);
                if (val < 0 || val > 255) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    // Validate severity value
    public static boolean isValidSeverity(String severity) {
        if (severity == null) return false;
        return severity.equals("LOW") || severity.equals("MEDIUM") ||
               severity.equals("HIGH") || severity.equals("CRITICAL");
    }

    // Validate alert source type
    public static boolean isValidSourceType(String sourceType) {
        if (sourceType == null) return false;
        return sourceType.equals("SPLUNK") || sourceType.equals("CROWDSTRIKE") ||
               sourceType.equals("FIREWALL") || sourceType.equals("CLOUD_SIEM");
    }

    // Validate incident state
    public static boolean isValidIncidentState(String state) {
        if (state == null) return false;
        return state.equals("NEW") || state.equals("UNDER_TRIAGE") ||
               state.equals("CONTAINMENT") || state.equals("ERADICATION") ||
               state.equals("RECOVERY") || state.equals("POST_INCIDENT_REVIEW") ||
               state.equals("CLOSED");
    }

    // Validate raw payload is not null or empty
    public static boolean isValidPayload(String payload) {
        return payload != null && !payload.trim().isEmpty();
    }
}
