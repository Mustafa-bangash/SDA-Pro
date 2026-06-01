package com.sdapro.incident;

import com.sdapro.incident.domain.state.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// PATTERN: State
public class StatePatternTest {

    @Test
    public void testIncidentStartsInNewState() {
        // Every incident should start in NEW state
        IncidentContext context = new IncidentContext("INC-001");
        assertEquals("NEW", context.getCurrentState().getStateName());
    }

    @Test
    public void testValidTransitionNewToUnderTriage() {
        // NEW -> UNDER_TRIAGE is a valid transition
        IncidentContext context = new IncidentContext("INC-001");
        context.beginTriage();
        assertEquals("UNDER_TRIAGE", context.getCurrentState().getStateName());
    }

    @Test
    public void testValidTransitionUnderTriageToContainment() {
        // UNDER_TRIAGE -> CONTAINMENT is a valid transition
        IncidentContext context = new IncidentContext("INC-001");
        context.beginTriage();
        context.initiateContainment();
        assertEquals("CONTAINMENT", context.getCurrentState().getStateName());
    }

    @Test
    public void testFullLifecycleTransition() {
        // Test full incident lifecycle New -> Closed
        IncidentContext context = new IncidentContext("INC-001");
        context.beginTriage();
        context.initiateContainment();
        context.beginEradication();
        context.beginRecovery();
        context.beginPostIncidentReview();
        context.close();
        assertEquals("CLOSED", context.getCurrentState().getStateName());
    }

    @Test
    public void testInvalidTransitionFromNewToContainment() {
        // Cannot skip triage and go directly to containment
        IncidentContext context = new IncidentContext("INC-001");
        assertThrows(IllegalStateException.class, () -> context.initiateContainment());
    }

    @Test
    public void testInvalidTransitionFromNewToClose() {
        // Cannot close incident from NEW state
        IncidentContext context = new IncidentContext("INC-001");
        assertThrows(IllegalStateException.class, () -> context.close());
    }

    @Test
    public void testCannotReopenClosedIncident() {
        // Once closed, no transitions are allowed
        IncidentContext context = new IncidentContext("INC-001");
        context.beginTriage();
        context.initiateContainment();
        context.beginEradication();
        context.beginRecovery();
        context.beginPostIncidentReview();
        context.close();
        assertThrows(IllegalStateException.class, () -> context.beginTriage());
    }

    @Test
    public void testStateHistoryIsRecorded() {
        // State transitions should be recorded in history
        IncidentContext context = new IncidentContext("INC-001");
        context.beginTriage();
        context.initiateContainment();
        String history = context.getStateHistory();
        assertTrue(history.contains("NEW"));
        assertTrue(history.contains("UNDER_TRIAGE"));
        assertTrue(history.contains("CONTAINMENT"));
    }
}
