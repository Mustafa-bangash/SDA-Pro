package com.sdapro.incident.domain.state;

// PATTERN: State (Concrete State 2)
// RATIONALE: Incident is being analyzed by SOC analyst.
// Only containment can be initiated from this state.
public class UnderTriageState implements IncidentState {

    @Override
    public void beginTriage(IncidentContext context) {
        throw new IllegalStateException("Incident is already under triage.");
    }

    @Override
    public void initiateContainment(IncidentContext context) {
        System.out.println("Initiating containment for incident: " + context.getIncidentId());
        context.setState(new ContainmentState());
    }

    @Override
    public void beginEradication(IncidentContext context) {
        throw new IllegalStateException("Cannot begin eradication before containment.");
    }

    @Override
    public void beginRecovery(IncidentContext context) {
        throw new IllegalStateException("Cannot begin recovery before containment.");
    }

    @Override
    public void beginPostIncidentReview(IncidentContext context) {
        throw new IllegalStateException("Cannot begin review before containment.");
    }

    @Override
    public void close(IncidentContext context) {
        throw new IllegalStateException("Cannot close incident during triage.");
    }

    @Override
    public String getStateName() { return "UNDER_TRIAGE"; }
}
