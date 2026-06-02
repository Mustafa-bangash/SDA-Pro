package com.sdapro.eventbus.publisher;

import com.sdapro.eventbus.subscribers.EventObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// PATTERN: Singleton + Observer
// RATIONALE: The event bus must be a single coordination point across all
// services. Multiple instances would cause events to be missed by subscribers.
// Singleton guarantees one publisher; Observer decouples publishers from
// subscribers so neither knows about the other directly.
public class EventBusPublisher {

    // PATTERN: Singleton — single instance
    private static EventBusPublisher instance;

    // PATTERN: Observer — registry of subscribers per event type
    private final Map<String, List<EventObserver>> subscribers;

    private EventBusPublisher() {
        this.subscribers = new HashMap<>();
    }

    // PATTERN: Singleton — thread-safe lazy initialization
    public static synchronized EventBusPublisher getInstance() {
        if (instance == null) {
            instance = new EventBusPublisher();
        }
        return instance;
    }

    // PATTERN: Observer — attach subscriber for an event type
    public void attach(String eventType, EventObserver observer) {
        subscribers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(observer);
        System.out.println("[EventBus] Subscribed: " + observer.getObserverId()
                + " to event=" + eventType);
    }

    // PATTERN: Observer — detach subscriber
    public void detach(String eventType, EventObserver observer) {
        List<EventObserver> list = subscribers.get(eventType);
        if (list != null) {
            list.remove(observer);
        }
    }

    // PATTERN: Observer — notify all subscribers for an event type
    public void publish(String eventType, String eventPayload) {
        System.out.println("[EventBus] Publishing event=" + eventType);
        List<EventObserver> list = subscribers.getOrDefault(eventType, new ArrayList<>());
        for (EventObserver observer : list) {
            observer.onEvent(eventType, eventPayload);
        }
    }

    // Convenience publishers for domain events
    public void publishAlertIngested(String alertId, String sourceType, String severity) {
        String payload = "alertId=" + alertId + ",source=" + sourceType + ",severity=" + severity;
        publish("AlertIngested", payload);
    }

    public void publishIncidentCreated(String incidentId, String severity) {
        String payload = "incidentId=" + incidentId + ",severity=" + severity;
        publish("IncidentCreated", payload);
    }

    public void publishIncidentStateChanged(String incidentId, String from, String to) {
        String payload = "incidentId=" + incidentId + ",from=" + from + ",to=" + to;
        publish("IncidentStateChanged", payload);
    }

    public void publishResponseActionExecuted(String incidentId, String action, String outcome) {
        String payload = "incidentId=" + incidentId + ",action=" + action + ",outcome=" + outcome;
        publish("ResponseActionExecuted", payload);
    }

    public void publishThreatIntelUpdated(String indicator, String verdict) {
        String payload = "indicator=" + indicator + ",verdict=" + verdict;
        publish("ThreatIntelUpdated", payload);
    }

    public int getSubscriberCount(String eventType) {
        return subscribers.getOrDefault(eventType, new ArrayList<>()).size();
    }
}
