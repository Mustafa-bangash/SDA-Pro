package com.sdapro.incident.domain.state;

// PATTERN: State (Concrete State 1)
// RATIONALE: Initial state of every incident. Only triage can
// be initiated from this state. All other transitions are invalid.
public class NewState implements IncidentState {

    @Override
    public void beginTriage(IncidentContext context) {
        System.out.println("Starting triage for incident: " + context.getIncidentId());
        context.setState(new UnderTriageState());
    }

    @Override
    public void initiateContainment(IncidentContext context) {
        throw new IllegalStateException("Cannot initiate containment from New state. Begin triage first.");
    }

    @Override
    public void beginEradication(IncidentContext context) {
        throw new IllegalStateException("Cannot begin eradication from New state.");
    }

    @Override
    public void beginRecovery(IncidentContext context) {
        throw new IllegalStateException("Cannot begin recovery from New state.");
    }

    @Override
    public void beginPostIncidentReview(IncidentContext context) {
        throw new IllegalStateException("Cannot begin review from New state.");
    }

    @Override
    public void close(IncidentContext context) {
        throw new IllegalStateException("Cannot close incident from New state.");
    }

    @Override
    public String getStateName() { return "NEW"; }
}
