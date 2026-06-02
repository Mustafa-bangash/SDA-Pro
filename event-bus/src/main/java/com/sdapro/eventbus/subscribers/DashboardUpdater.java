package com.sdapro.eventbus.subscribers;

// PATTERN: Observer — Concrete Observer
// RATIONALE: Dashboard must reflect real-time state changes without
// polling. DashboardUpdater subscribes to all incident and alert events
// and pushes updates via WebSocket to connected SOC analysts.
// The dashboard never calls services directly — it only reacts to events.
public class DashboardUpdater implements EventObserver {

    private int updateCount = 0;
    private String lastEvent;

    @Override
    public void onEvent(String eventType, String eventPayload) {
        updateCount++;
        lastEvent = eventType;

        switch (eventType) {
            case "AlertIngested":
                System.out.println("[Dashboard] New alert received — refreshing alert stream. Payload=" + eventPayload);
                pushAlertStreamUpdate(eventPayload);
                break;
            case "IncidentCreated":
                System.out.println("[Dashboard] New incident created — updating incident queue. Payload=" + eventPayload);
                pushIncidentQueueUpdate(eventPayload);
                break;
            case "IncidentStateChanged":
                System.out.println("[Dashboard] Incident state changed — updating timeline. Payload=" + eventPayload);
                pushStateChangeUpdate(eventPayload);
                break;
            case "ResponseActionExecuted":
                System.out.println("[Dashboard] Response action executed — updating response console. Payload=" + eventPayload);
                pushResponseUpdate(eventPayload);
                break;
            default:
                System.out.println("[Dashboard] Unknown event=" + eventType);
        }
    }

    private void pushAlertStreamUpdate(String payload) {
        // Real impl: websocketManager.broadcast("alert-stream", payload)
        System.out.println("[Dashboard] WebSocket push → alert-stream channel");
    }

    private void pushIncidentQueueUpdate(String payload) {
        System.out.println("[Dashboard] WebSocket push → incident-queue channel");
    }

    private void pushStateChangeUpdate(String payload) {
        System.out.println("[Dashboard] WebSocket push → incident-timeline channel");
    }

    private void pushResponseUpdate(String payload) {
        System.out.println("[Dashboard] WebSocket push → response-console channel");
    }

    @Override
    public String getObserverId() {
        return "DashboardUpdater";
    }

    public int getUpdateCount() { return updateCount; }
    public String getLastEvent() { return lastEvent; }
}
