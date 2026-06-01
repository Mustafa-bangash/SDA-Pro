package com.sdapro.notification.channels;

public class SlackNotifier implements Notifier {
    @Override
    public void send(String message) {
        System.out.println("[SLACK] Posting message to #security-alerts: " + message);
    }
}
