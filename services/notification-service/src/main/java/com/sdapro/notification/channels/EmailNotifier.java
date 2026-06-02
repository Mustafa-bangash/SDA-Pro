package com.sdapro.notification.channels;

// PATTERN: Abstract Factory
// Concrete Product — created by EnterpriseNotificationFactory
// Email channel for SOC analyst notifications
public class EmailNotifier implements Notifier {

    private final String smtpHost;
    private final String fromAddress;

    public EmailNotifier(String smtpHost, String fromAddress) {
        this.smtpHost = smtpHost;
        this.fromAddress = fromAddress;
    }

    // Default constructor for BasicNotificationFactory
    public EmailNotifier() {
        this.smtpHost = "localhost";
        this.fromAddress = "soc-alerts@sdapro.local";
    }

    @Override
    public String notify(String recipient, String subject, String message) {
        // Simulate SMTP send
        String log = "[EMAIL] To=" + recipient
                + " Subject=" + subject
                + " Via=" + smtpHost
                + " From=" + fromAddress
                + " Status=SENT";
        System.out.println(log);
        return "EMAIL_SENT";
    }

    @Override
    public String getChannelType() {
        return "EMAIL";
    }
}
