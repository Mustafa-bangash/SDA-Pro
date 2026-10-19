package com.sdapro.eventbus.subscribers;

// PATTERN: Observer

public interface EventSubscriber {

    void handle(Object event);

}
