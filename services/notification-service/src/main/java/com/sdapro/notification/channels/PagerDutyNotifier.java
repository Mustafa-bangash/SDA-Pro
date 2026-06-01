package com.sdapro.notification.channels;

public class PagerDutyNotifier implements Notifier {
    @Override
    public void send(String message) {
        System.out.println("[PAGERDUTY] Waking up the on-call engineer! Alert: " + message);
    }
}
