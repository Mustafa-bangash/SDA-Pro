package com.sdapro.eventbus.subscribers;

// PATTERN: Observer

public class MetricsCollector implements EventSubscriber {

    @Override
    public void handle(Object event) {

        System.out.println(
            "Metrics updated: " + event
        );

    }
}
