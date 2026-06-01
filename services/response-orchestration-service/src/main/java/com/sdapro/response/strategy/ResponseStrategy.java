package com.sdapro.response.strategy;
import java.util.List;

// PATTERN: Strategy
// RATIONALE: We need interchangeable response algorithms based on how severe the incident is.
public interface ResponseStrategy {
    List<String> determineActions(String incidentSeverity);
}
