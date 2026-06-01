package com.sdapro.incident.domain.state;

// PATTERN: State (Concrete State 5)
// RATIONALE: Systems are being restored to normal operation.
// Services restarted, backups restored in this state.
public class RecoveryState implements IncidentState {

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
        throw new IllegalStateException("Recovery already in progress.");
    }

    @Override
    public void beginPostIncidentReview(IncidentContext context) {
        System.out.println("Starting post incident review for: " + context.getIncidentId());
        context.setState(new PostIncidentReviewState());
    }

    @Override
    public void close(IncidentContext context) {
        throw new IllegalStateException("Cannot close incident during recovery.");
    }

    @Override
    public String getStateName() { return "RECOVERY"; }
}
