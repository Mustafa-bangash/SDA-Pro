package com.sdapro.eventbus.subscribers;

// PATTERN: Observer
// Subject interface that all concrete observers implement
public interface EventObserver {
    void onEvent(String eventType, String eventPayload);
    String getObserverId();
}
