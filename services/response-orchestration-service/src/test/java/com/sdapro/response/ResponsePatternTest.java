package com.sdapro.response;

import com.sdapro.response.strategy.*;
import com.sdapro.response.decorator.*;
import com.sdapro.response.facade.IncidentResponseFacade;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ResponsePatternTest {

    @Test
    public void testStrategySelection() {
        ResponseStrategy aggressive = new AggressiveContainmentStrategy();
        List<String> aggressiveActions = aggressive.determineActions("CRITICAL");
        assertTrue(aggressiveActions.contains("BlockIP"));

        ResponseStrategy conservative = new ConservativeStrategy();
        List<String> conservativeActions = conservative.determineActions("LOW");
        assertTrue(conservativeActions.contains("MonitorActivity"));
    }

    @Test
    public void testDecoratorWrapsAction() {
        ResponseAction baseAction = new BlockIPAction();
        ResponseAction decoratedAction = new AuditLogDecorator(baseAction);
        
        // Should not throw any exceptions when executing the wrapped action
        assertDoesNotThrow(() -> decoratedAction.execute());
    }

    @Test
    public void testFacadeExecutesWithoutError() {
        IncidentResponseFacade facade = new IncidentResponseFacade();
        assertDoesNotThrow(() -> facade.assessAndRespond("CRITICAL"));
    }
}
