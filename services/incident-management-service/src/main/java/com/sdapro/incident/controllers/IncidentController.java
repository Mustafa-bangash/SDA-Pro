package com.sdapro.incident.controllers;

import com.sdapro.incident.domain.state.IncidentContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// MVC: Controller layer for Incident Management Service
// PATTERN: State pattern triggered through IncidentContext here
@RestController
@RequestMapping("/api/v1/incidents")
public class IncidentController {

    // In-memory store for demo purposes
    private Map<String, IncidentContext> incidentStore = new HashMap<>();

    // POST /api/v1/incidents
    // Creates a new incident
    @PostMapping
    public ResponseEntity<Map<String, String>> createIncident(@RequestBody Map<String, String> payload) {
        String incidentId = "INC-" + System.currentTimeMillis();

        // PATTERN: State - IncidentContext starts in NewState automatically
        IncidentContext context = new IncidentContext(incidentId);
        incidentStore.put(incidentId, context);

        Map<String, String> response = new HashMap<>();
        response.put("incidentId", incidentId);
        response.put("state", context.getCurrentState().getStateName());
        response.put("message", "Incident created successfully");
        return ResponseEntity.ok(response);
    }

    // GET /api/v1/incidents
    // Returns all incidents with their current state
    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getAllIncidents() {
        List<Map<String, String>> incidents = new ArrayList<>();
        for (Map.Entry<String, IncidentContext> entry : incidentStore.entrySet()) {
            Map<String, String> incident = new HashMap<>();
            incident.put("incidentId", entry.getKey());
            incident.put("currentState", entry.getValue().getCurrentState().getStateName());
            incident.put("stateHistory", entry.getValue().getStateHistory());
            incidents.add(incident);
        }
        return ResponseEntity.ok(incidents);
    }

    // GET /api/v1/incidents/{id}
    // Returns a specific incident
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getIncident(@PathVariable String id) {
        IncidentContext context = incidentStore.get(id);
        if (context == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, String> response = new HashMap<>();
        response.put("incidentId", id);
        response.put("currentState", context.getCurrentState().getStateName());
        response.put("stateHistory", context.getStateHistory());
        return ResponseEntity.ok(response);
    }

    // POST /api/v1/incidents/{id}/triage
    // Transitions incident to UnderTriage state
    @PostMapping("/{id}/triage")
    public ResponseEntity<Map<String, String>> beginTriage(@PathVariable String id) {
        IncidentContext context = incidentStore.get(id);
        if (context == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            // PATTERN: State - delegates to current state
            context.beginTriage();
            Map<String, String> response = new HashMap<>();
            response.put("incidentId", id);
            response.put("newState", context.getCurrentState().getStateName());
            response.put("message", "Triage started successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/v1/incidents/{id}/containment
    // Transitions incident to Containment state
    @PostMapping("/{id}/containment")
    public ResponseEntity<Map<String, String>> initiateContainment(@PathVariable String id) {
        IncidentContext context = incidentStore.get(id);
        if (context == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            context.initiateContainment();
            Map<String, String> response = new HashMap<>();
            response.put("incidentId", id);
            response.put("newState", context.getCurrentState().getStateName());
            response.put("message", "Containment initiated successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/v1/incidents/{id}/eradication
    // Transitions incident to Eradication state
    @PostMapping("/{id}/eradication")
    public ResponseEntity<Map<String, String>> beginEradication(@PathVariable String id) {
        IncidentContext context = incidentStore.get(id);
        if (context == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            context.beginEradication();
            Map<String, String> response = new HashMap<>();
            response.put("incidentId", id);
            response.put("newState", context.getCurrentState().getStateName());
            response.put("message", "Eradication started successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/v1/incidents/{id}/recovery
    // Transitions incident to Recovery state
    @PostMapping("/{id}/recovery")
    public ResponseEntity<Map<String, String>> beginRecovery(@PathVariable String id) {
        IncidentContext context = incidentStore.get(id);
        if (context == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            context.beginRecovery();
            Map<String, String> response = new HashMap<>();
            response.put("incidentId", id);
            response.put("newState", context.getCurrentState().getStateName());
            response.put("message", "Recovery started successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/v1/incidents/{id}/review
    // Transitions incident to PostIncidentReview state
    @PostMapping("/{id}/review")
    public ResponseEntity<Map<String, String>> beginReview(@PathVariable String id) {
        IncidentContext context = incidentStore.get(id);
        if (context == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            context.beginPostIncidentReview();
            Map<String, String> response = new HashMap<>();
            response.put("incidentId", id);
            response.put("newState", context.getCurrentState().getStateName());
            response.put("message", "Post incident review started");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/v1/incidents/{id}/close
    // Transitions incident to Closed state
    @PostMapping("/{id}/close")
    public ResponseEntity<Map<String, String>> closeIncident(@PathVariable String id) {
        IncidentContext context = incidentStore.get(id);
        if (context == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            context.close();
            Map<String, String> response = new HashMap<>();
            response.put("incidentId", id);
            response.put("newState", context.getCurrentState().getStateName());
            response.put("message", "Incident closed successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/v1/incidents/health
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("incident-management-service is running");
    }
}
