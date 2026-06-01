package com.sdapro.incident.domain.state;

// PATTERN: State (Concrete State 6)
// RATIONALE: Incident is being reviewed for lessons learned.
// Reports and compliance evidence collected in this state.
public class PostIncidentReviewState implements IncidentState {

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
        throw new IllegalStateException("Incident is already past eradication.");
    }

    @Override
    public void beginRecovery(IncidentContext context) {
        throw new IllegalStateException("Incident is already past recovery.");
    }

    @Override
    public void beginPostIncidentReview(IncidentContext context) {
        throw new IllegalStateException("Post incident review already in progress.");
    }

    @Override
    public void close(IncidentContext context) {
        System.out.println("Closing incident: " + context.getIncidentId());
        context.setState(new ClosedState());
    }

    @Override
    public String getStateName() { return "POST_INCIDENT_REVIEW"; }
}
