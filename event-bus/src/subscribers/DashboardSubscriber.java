package com.sdapro.eventbus.subscribers;

// PATTERN: Observer

public class DashboardSubscriber implements EventSubscriber {

    @Override
    public void handle(Object event) {

        System.out.println(
            "Dashboard updated: " + event
        );

    }
}
