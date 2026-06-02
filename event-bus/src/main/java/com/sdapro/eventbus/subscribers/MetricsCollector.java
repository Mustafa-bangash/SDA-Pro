package com.sdapro.eventbus.subscribers;

// PATTERN: Observer — Concrete Observer
// Collects SOC metrics from domain events for reporting
public class MetricsCollector implements EventObserver {

    private int alertsIngested = 0;
    private int incidentsCreated = 0;
    private int stateTransitions = 0;
    private int responseActionsExecuted = 0;

    @Override
    public void onEvent(String eventType, String eventPayload) {
        switch (eventType) {
            case "AlertIngested":
                alertsIngested++;
                System.out.println("[Metrics] alertsIngested=" + alertsIngested);
                break;
            case "IncidentCreated":
                incidentsCreated++;
                System.out.println("[Metrics] incidentsCreated=" + incidentsCreated);
                break;
            case "IncidentStateChanged":
                stateTransitions++;
                System.out.println("[Metrics] stateTransitions=" + stateTransitions);
                break;
            case "ResponseActionExecuted":
                responseActionsExecuted++;
                System.out.println("[Metrics] responseActionsExecuted=" + responseActionsExecuted);
                break;
        }
    }

    @Override
    public String getObserverId() { return "MetricsCollector"; }

    public int getAlertsIngested() { return alertsIngested; }
    public int getIncidentsCreated() { return incidentsCreated; }
    public int getStateTransitions() { return stateTransitions; }
    public int getResponseActionsExecuted() { return responseActionsExecuted; }
}
