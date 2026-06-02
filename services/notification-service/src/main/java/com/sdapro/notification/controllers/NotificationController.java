package com.sdapro.notification.controllers;

import com.sdapro.notification.factory.*;
import com.sdapro.notification.channels.Notifier;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    // Using the Enterprise Combo Meal!
    private final NotificationFactory factory = new EnterpriseNotificationFactory();

    @PostMapping("/dispatch")
    public String dispatchNotification(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        
        Notifier email = factory.createEmailNotifier();
        if (email != null) email.send(message);
        
        Notifier slack = factory.createSlackNotifier();
        if (slack != null) slack.send(message);
        
        Notifier pagerDuty = factory.createPagerDutyNotifier();
        if (pagerDuty != null) pagerDuty.send(message);
        
        return "Notifications dispatched successfully.";
    }
    
    @GetMapping("/health")
    public String health() {
        return "notification-service is running";
    }
}
