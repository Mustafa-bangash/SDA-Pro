package com.sdapro.threatintel.controllers;

import com.sdapro.threatintel.services.proxy.CachingProxy;
import com.sdapro.threatintel.services.adapter.VirusTotalAdapter;
import com.sdapro.threatintel.domain.reputation.ReputationResult;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/threat-intel")
public class ThreatIntelController {

    // Injecting the Proxy which wraps the Adapter
    private final CachingProxy proxy = new CachingProxy(new VirusTotalAdapter());

    @PostMapping("/reputation")
    public ReputationResult checkReputation(@RequestBody Map<String, String> request) {
        String indicator = request.get("indicator");
        return proxy.checkReputation(indicator);
    }
    
    @GetMapping("/health")
    public String health() {
        return "threat-intel-service is running";
    }
}
