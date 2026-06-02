package com.sdapro.identity.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

// Layered Architecture: Presentation Layer
// Handles analyst authentication and authorization for the SOC dashboard
@RestController
@RequestMapping("/api/v1/identity")
public class IdentityController {

    // POST /api/v1/identity/login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.getOrDefault("username", "");
        String password = credentials.getOrDefault("password", "");

        // Simulate authentication (production: validate against AD/LDAP)
        if (username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        String token = UUID.randomUUID().toString();
        String role = username.contains("senior") ? "SENIOR_ANALYST" : "ANALYST";

        return ResponseEntity.ok(Map.of(
            "token", token,
            "analystId", UUID.randomUUID().toString(),
            "username", username,
            "role", role
        ));
    }

    // POST /api/v1/identity/logout
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        // Real impl: invalidate token in Redis session store
        return ResponseEntity.ok(Map.of("status", "logged out"));
    }

    // GET /api/v1/identity/analysts/{id}
    @GetMapping("/analysts/{id}")
    public ResponseEntity<Map<String, String>> getAnalyst(@PathVariable String id) {
        // Real impl: query analyst profile from DB
        return ResponseEntity.ok(Map.of(
            "analystId", id,
            "username", "analyst-" + id.substring(0, 8),
            "role", "ANALYST",
            "clearanceLevel", "LEVEL_2"
        ));
    }

    // GET /api/v1/identity/health
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("identity-service is running");
    }
}
