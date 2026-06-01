package com.sdapro.notification.factory;
import com.sdapro.notification.channels.*;

public class EnterpriseNotificationFactory implements NotificationFactory {
    @Override
    public Notifier createEmailNotifier() { return new EmailNotifier(); }
    
    @Override
    public Notifier createSlackNotifier() { return new SlackNotifier(); }
    
    @Override
    public Notifier createPagerDutyNotifier() { return new PagerDutyNotifier(); }
}
