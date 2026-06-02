package com.sdapro.notification.factory;
import com.sdapro.notification.channels.*;

public class BasicNotificationFactory implements NotificationFactory {
    @Override
    public Notifier createEmailNotifier() { return new EmailNotifier(); }
    
    @Override
    public Notifier createSlackNotifier() { 
        System.out.println("[DISABLED] Slack is not available on the Basic plan.");
        return null; 
    }
    
    @Override
    public Notifier createPagerDutyNotifier() { 
        System.out.println("[DISABLED] PagerDuty is not available on the Basic plan.");
        return null; 
    }
}
