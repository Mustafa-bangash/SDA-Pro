package com.sdapro.incident.domain.state;

// PATTERN: State (Concrete State 3)
// RATIONALE: Threat is being contained. Response actions like
// blocking IPs and isolating endpoints are executed in this state.
public class ContainmentState implements IncidentState {

    @Override
    public void beginTriage(IncidentContext context) {
        throw new IllegalStateException("Incident is already past triage.");
    }

    @Override
    public void initiateContainment(IncidentContext context) {
        throw new IllegalStateException("Containment already in progress.");
    }

    @Override
    public void beginEradication(IncidentContext context) {
        System.out.println("Starting eradication for incident: " + context.getIncidentId());
        context.setState(new EradicationState());
    }

    @Override
    public void beginRecovery(IncidentContext context) {
        throw new IllegalStateException("Cannot begin recovery before eradication.");
    }

    @Override
    public void beginPostIncidentReview(IncidentContext context) {
        throw new IllegalStateException("Cannot begin review before eradication.");
    }

    @Override
    public void close(IncidentContext context) {
        throw new IllegalStateException("Cannot close incident during containment.");
    }

    @Override
    public String getStateName() { return "CONTAINMENT"; }
}
