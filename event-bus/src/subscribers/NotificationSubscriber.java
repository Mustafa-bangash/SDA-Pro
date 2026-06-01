package com.sdapro.eventbus.subscribers;

// PATTERN: Observer

public class NotificationSubscriber implements EventSubscriber {

    @Override
    public void handle(Object event) {

        System.out.println(
            "Notification sent: " + event
        );

    }
}
