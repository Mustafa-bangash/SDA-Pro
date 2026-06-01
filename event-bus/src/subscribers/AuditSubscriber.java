package com.sdapro.eventbus.subscribers;

// PATTERN: Observer

public class AuditSubscriber implements EventSubscriber {

    @Override
    public void handle(Object event) {

        System.out.println(
            "Audit log created: " + event
        );

    }
}
