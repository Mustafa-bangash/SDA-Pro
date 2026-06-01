package com.sdapro.threatintel;

import com.sdapro.threatintel.services.adapter.VirusTotalAdapter;
import com.sdapro.threatintel.services.proxy.CachingProxy;
import com.sdapro.threatintel.domain.reputation.ReputationResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThreatIntelPatternTest {

    @Test
    public void testAdapterTranslatesCorrectly() {
        VirusTotalAdapter adapter = new VirusTotalAdapter();
        ReputationResult result = adapter.checkReputation("192.168.1.100");
        
        assertEquals("MALICIOUS", result.getVerdict());
        assertEquals(95, result.getConfidenceScore());
    }

    @Test
    public void testProxyCachesResults() {
        VirusTotalAdapter realAdapter = new VirusTotalAdapter();
        CachingProxy proxy = new CachingProxy(realAdapter);
        
        // First call should hit the real adapter
        ReputationResult result1 = proxy.checkReputation("10.0.0.30");
        assertEquals("SUSPICIOUS", result1.getVerdict());
        
        // Second call should hit the cache (it will return the exact same object)
        ReputationResult result2 = proxy.checkReputation("10.0.0.30");
        assertSame(result1, result2);
    }
}
