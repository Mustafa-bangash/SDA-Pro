package com.sdapro.eventbus;

import com.sdapro.eventbus.publisher.EventBusPublisher;
import com.sdapro.eventbus.subscribers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// PATTERN: Observer
// Tests that EventBusPublisher correctly notifies all registered subscribers
public class ObserverPatternTest {

    private EventBusPublisher publisher;
    private DashboardUpdater dashboard;
    private AuditLogger auditLogger;
    private NotificationDispatcher notificationDispatcher;
    private MetricsCollector metricsCollector;

    @BeforeEach
    public void setUp() {
        publisher = EventBusPublisher.getInstance();
        dashboard = new DashboardUpdater();
        auditLogger = new AuditLogger();
        notificationDispatcher = new NotificationDispatcher();
        metricsCollector = new MetricsCollector();

        publisher.attach("AlertIngested", dashboard);
        publisher.attach("AlertIngested", auditLogger);
        publisher.attach("AlertIngested", metricsCollector);
        publisher.attach("IncidentCreated", dashboard);
        publisher.attach("IncidentCreated", auditLogger);
        publisher.attach("IncidentCreated", notificationDispatcher);
        publisher.attach("IncidentCreated", metricsCollector);
        publisher.attach("IncidentStateChanged", dashboard);
        publisher.attach("IncidentStateChanged", auditLogger);
        publisher.attach("IncidentStateChanged", notificationDispatcher);
        publisher.attach("IncidentStateChanged", metricsCollector);
        publisher.attach("ResponseActionExecuted", dashboard);
        publisher.attach("ResponseActionExecuted", auditLogger);
        publisher.attach("ResponseActionExecuted", notificationDispatcher);
        publisher.attach("ResponseActionExecuted", metricsCollector);
    }

    @Test
    public void testSingletonInstance() {
        EventBusPublisher instance1 = EventBusPublisher.getInstance();
        EventBusPublisher instance2 = EventBusPublisher.getInstance();
        assertSame(instance1, instance2, "EventBusPublisher must be a Singleton");
    }

    @Test
    public void testAlertIngestedNotifiesAllSubscribers() {
        publisher.publishAlertIngested("alert-001", "SPLUNK", "CRITICAL");
        assertEquals(1, metricsCollector.getAlertsIngested());
        assertTrue(auditLogger.getEntryCount() >= 1);
        assertEquals("AlertIngested", dashboard.getLastEvent());
    }

    @Test
    public void testIncidentCreatedNotifiesAllSubscribers() {
        publisher.publishIncidentCreated("inc-001", "CRITICAL");
        assertEquals(1, metricsCollector.getIncidentsCreated());
        assertEquals(1, notificationDispatcher.getDispatchCount());
        assertTrue(auditLogger.getEntryCount() >= 1);
    }

    @Test
    public void testIncidentStateChangedNotifiesSubscribers() {
        publisher.publishIncidentStateChanged("inc-001", "NEW", "UNDER_TRIAGE");
        assertEquals(1, metricsCollector.getStateTransitions());
        assertTrue(notificationDispatcher.getDispatchCount() >= 1);
    }

    @Test
    public void testResponseActionExecutedNotifiesSubscribers() {
        publisher.publishResponseActionExecuted("inc-001", "BLOCK_IP", "SUCCESS");
        assertEquals(1, metricsCollector.getResponseActionsExecuted());
        assertTrue(auditLogger.getEntryCount() >= 1);
    }

    @Test
    public void testAuditTrailIsImmutableLog() {
        publisher.publishAlertIngested("alert-002", "FIREWALL", "HIGH");
        publisher.publishIncidentCreated("inc-002", "HIGH");
        assertTrue(auditLogger.getEntryCount() >= 2);
        auditLogger.getAuditTrail().forEach(entry ->
            assertTrue(entry.contains("immutable=true")));
    }
}
