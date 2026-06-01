package com.sdapro.incident.domain.state;

// PATTERN: State (State Interface)
// RATIONALE: Defines the contract for all incident states.
// Each state implements only the transitions that are valid
// from that state, throwing exceptions for invalid ones.
public interface IncidentState {
    void beginTriage(IncidentContext context);
    void initiateContainment(IncidentContext context);
    void beginEradication(IncidentContext context);
    void beginRecovery(IncidentContext context);
    void beginPostIncidentReview(IncidentContext context);
    void close(IncidentContext context);
    String getStateName();
}
