package com.sdapro.incident.domain.state;

// PATTERN: State (Concrete State 4)
// RATIONALE: Threat is being removed from the system.
// Malicious files quarantined, accounts disabled in this state.
public class EradicationState implements IncidentState {

    @Override
    public void beginTriage(IncidentContext context) {
        throw new IllegalStateException("Incident is already past triage.");
    }

    @Override
    public void initiateContainment(IncidentContext context) {
        throw new IllegalStateException("Incident is already past containment.");
    }

    @Override
    public void beginEradication(IncidentContext context) {
        throw new IllegalStateException("Eradication already in progress.");
    }

    @Override
    public void beginRecovery(IncidentContext context) {
        System.out.println("Starting recovery for incident: " + context.getIncidentId());
        context.setState(new RecoveryState());
    }

    @Override
    public void beginPostIncidentReview(IncidentContext context) {
        throw new IllegalStateException("Cannot begin review before recovery.");
    }

    @Override
    public void close(IncidentContext context) {
        throw new IllegalStateException("Cannot close incident during eradication.");
    }

    @Override
    public String getStateName() { return "ERADICATION"; }
}
