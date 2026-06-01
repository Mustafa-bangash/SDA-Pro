package com.sdapro.notification.factory;
import com.sdapro.notification.channels.*;

// PATTERN: Abstract Factory
// RATIONALE: Creates families of related notification channels (e.g., all Enterprise channels together) 
// without specifying their concrete classes in the main code.
public interface NotificationFactory {
    Notifier createEmailNotifier();
    Notifier createSlackNotifier();
    Notifier createPagerDutyNotifier();
}
