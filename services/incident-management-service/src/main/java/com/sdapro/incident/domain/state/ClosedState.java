package com.sdapro.incident.domain.state;

// PATTERN: State (Concrete State 7 - Final State)
// RATIONALE: Terminal state. No further transitions are allowed.
// Incident is fully resolved and documented.
public class ClosedState implements IncidentState {

    @Override
    public void beginTriage(IncidentContext context) {
        throw new IllegalStateException("Incident is already closed.");
    }

    @Override
    public void initiateContainment(IncidentContext context) {
        throw new IllegalStateException("Incident is already closed.");
    }

    @Override
    public void beginEradication(IncidentContext context) {
        throw new IllegalStateException("Incident is already closed.");
    }

    @Override
    public void beginRecovery(IncidentContext context) {
        throw new IllegalStateException("Incident is already closed.");
    }

    @Override
    public void beginPostIncidentReview(IncidentContext context) {
        throw new IllegalStateException("Incident is already closed.");
    }

    @Override
    public void close(IncidentContext context) {
        throw new IllegalStateException("Incident is already closed.");
    }

    @Override
    public String getStateName() { return "CLOSED"; }
}
