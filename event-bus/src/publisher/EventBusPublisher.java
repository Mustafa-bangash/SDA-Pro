package com.sdapro.eventbus.publisher;

// PATTERN: Singleton
// PATTERN: Observer

public class EventBusPublisher {

    private static EventBusPublisher instance;

    private EventBusPublisher() {
    }

    public static synchronized EventBusPublisher getInstance() {

        if (instance == null) {
            instance = new EventBusPublisher();
        }

        return instance;
    }

    public void publish(String eventType, Object event) {

        System.out.println(
            "Publishing event: " + eventType
        );

    }
}
