package com.sdapro.shared.commons;

import java.time.Instant;

// Shared logging utility used across all services
// Provides consistent log format with service name, timestamp, and pattern annotations
public class LoggingUtils {

    // Log format: [TIMESTAMP] [SERVICE] [PATTERN] MESSAGE
    public static String formatLog(String serviceName, String patternName, String message) {
        return "[" + Instant.now() + "] [" + serviceName + "] [PATTERN:" + patternName + "] " + message;
    }

    public static void logPatternUsage(String patternName, String className, String action) {
        System.out.println(formatLog("SDA-Pro", patternName,
            "Class=" + className + " Action=" + action));
    }

    public static void logEventPublished(String serviceName, String eventType, String eventId) {
        System.out.println(formatLog(serviceName, "Observer",
            "Published event=" + eventType + " id=" + eventId));
    }

    public static void logEventConsumed(String serviceName, String eventType, String eventId) {
        System.out.println(formatLog(serviceName, "Observer",
            "Consumed event=" + eventType + " id=" + eventId));
    }

    public static void logStateTransition(String incidentId, String from, String to) {
        System.out.println(formatLog("incident-management-service", "State",
            "Incident=" + incidentId + " transition=" + from + "->" + to));
    }
}
