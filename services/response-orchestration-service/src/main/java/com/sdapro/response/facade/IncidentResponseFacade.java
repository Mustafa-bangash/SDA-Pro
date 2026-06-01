package com.sdapro.response.facade;
import com.sdapro.response.strategy.*;
import com.sdapro.response.decorator.*;
import java.util.List;

// PATTERN: Facade
// RATIONALE: Simplifies the complex subsystem of selecting a strategy, applying decorators, and executing actions.
public class IncidentResponseFacade {

    public void assessAndRespond(String severity) {
        System.out.println("\n--- FACADE: Starting Response for " + severity + " Incident ---");

        ResponseStrategy strategy;
        if ("CRITICAL".equalsIgnoreCase(severity)) {
            strategy = new AggressiveContainmentStrategy();
        } else {
            strategy = new ConservativeStrategy();
        }

        List<String> actions = strategy.determineActions(severity);
        System.out.println("Facade picked these actions: " + String.join(", ", actions));

        if (actions.contains("BlockIP")) {
            ResponseAction rawAction = new BlockIPAction();
            ResponseAction securedAction = new AuditLogDecorator(rawAction);
            securedAction.execute(); 
        }
    }
}
