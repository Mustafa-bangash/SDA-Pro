package com.sdapro.shared.commons;

// Shared error handler used across all services
// Provides consistent error response structure
public class ErrorHandler {

    public static class ServiceException extends RuntimeException {
        private final String errorCode;
        private final String serviceName;

        public ServiceException(String serviceName, String errorCode, String message) {
            super(message);
            this.errorCode = errorCode;
            this.serviceName = serviceName;
        }

        public String getErrorCode() { return errorCode; }
        public String getServiceName() { return serviceName; }
    }

    public static class InvalidAlertException extends ServiceException {
        public InvalidAlertException(String message) {
            super("alert-ingestion-service", "INVALID_ALERT", message);
        }
    }

    public static class InvalidStateTransitionException extends ServiceException {
        public InvalidStateTransitionException(String from, String to) {
            super("incident-management-service", "INVALID_STATE_TRANSITION",
                  "Cannot transition from " + from + " to " + to);
        }
    }

    public static class EnrichmentPipelineException extends ServiceException {
        public EnrichmentPipelineException(String message) {
            super("enrichment-correlation-service", "ENRICHMENT_FAILED", message);
        }
    }

    public static class ExternalApiException extends ServiceException {
        public ExternalApiException(String apiName, String message) {
            super("threat-intel-service", "EXTERNAL_API_ERROR",
                  "API=" + apiName + " Error=" + message);
        }
    }
}
