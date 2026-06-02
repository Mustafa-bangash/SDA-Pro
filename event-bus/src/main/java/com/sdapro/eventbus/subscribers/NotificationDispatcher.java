package com.sdapro.eventbus.subscribers;

// PATTERN: Observer — Concrete Observer
// RATIONALE: Analysts must be notified when critical incidents are created
// or when response actions complete. NotificationDispatcher subscribes to
// relevant events and triggers the notification-service without any
// direct coupling to the incident or response services.
public class NotificationDispatcher implements EventObserver {

    private int dispatchCount = 0;

    @Override
    public void onEvent(String eventType, String eventPayload) {
        switch (eventType) {
            case "IncidentCreated":
                dispatchCount++;
                System.out.println("[NotificationDispatcher] Critical incident created — dispatching PagerDuty alert. Payload=" + eventPayload);
                dispatchPagerDuty(eventPayload);
                break;
            case "IncidentStateChanged":
                dispatchCount++;
                System.out.println("[NotificationDispatcher] Incident state changed — dispatching Slack notification. Payload=" + eventPayload);
                dispatchSlack(eventPayload);
                break;
            case "ResponseActionExecuted":
                dispatchCount++;
                System.out.println("[NotificationDispatcher] Response action completed — dispatching email summary. Payload=" + eventPayload);
                dispatchEmail(eventPayload);
                break;
            default:
                // Not all events require notifications
                break;
        }
    }

    private void dispatchPagerDuty(String payload) {
        // Real impl: notificationService.dispatch("PAGERDUTY", payload)
        System.out.println("[NotificationDispatcher] PagerDuty triggered");
    }

    private void dispatchSlack(String payload) {
        System.out.println("[NotificationDispatcher] Slack message sent");
    }

    private void dispatchEmail(String payload) {
        System.out.println("[NotificationDispatcher] Email sent to analyst");
    }

    @Override
    public String getObserverId() {
        return "NotificationDispatcher";
    }

    public int getDispatchCount() { return dispatchCount; }
}
