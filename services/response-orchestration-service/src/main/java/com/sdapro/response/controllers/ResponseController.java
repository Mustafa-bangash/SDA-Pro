package com.sdapro.response.controllers;

import com.sdapro.response.facade.IncidentResponseFacade;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/response")
public class ResponseController {
    
    // Using our Facade to hide all the complex logic!
    private final IncidentResponseFacade facade = new IncidentResponseFacade();

    @PostMapping("/{incidentId}/assess")
    public String assessAndRespond(@PathVariable String incidentId, @RequestParam String severity) {
        System.out.println("Received request to assess incident: " + incidentId);
        facade.assessAndRespond(severity);
        return "Response plan executed for incident " + incidentId;
    }
    
    @GetMapping("/health")
    public String health() {
        return "response-orchestration-service is running";
    }
}
