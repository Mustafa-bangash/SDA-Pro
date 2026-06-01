package com.sdapro.incident.domain.state;

// PATTERN: State (Context)
// RATIONALE: Holds the current state and delegates all
// state-specific behavior to the current state object.
public class IncidentContext {

    private IncidentState currentState;
    private String incidentId;
    private String stateHistory;

    public IncidentContext(String incidentId) {
        this.incidentId = incidentId;
        this.stateHistory = "";
        // All incidents start in NewState
        this.currentState = new NewState();
        logTransition("CREATED", currentState.getStateName());
    }

    // PATTERN: State
    // Delegates behavior to current state
    public void setState(IncidentState state) {
        logTransition(currentState.getStateName(), state.getStateName());
        this.currentState = state;
    }

    public void beginTriage() {
        currentState.beginTriage(this);
    }

    public void initiateContainment() {
        currentState.initiateContainment(this);
    }

    public void beginEradication() {
        currentState.beginEradication(this);
    }

    public void beginRecovery() {
        currentState.beginRecovery(this);
    }

    public void beginPostIncidentReview() {
        currentState.beginPostIncidentReview(this);
    }

    public void close() {
        currentState.close(this);
    }

    private void logTransition(String from, String to) {
        stateHistory += from + " -> " + to + "\n";
        System.out.println("[Incident " + incidentId + "] State: " + from + " -> " + to);
    }

    public IncidentState getCurrentState() { return currentState; }
    public String getIncidentId() { return incidentId; }
    public String getStateHistory() { return stateHistory; }
}
