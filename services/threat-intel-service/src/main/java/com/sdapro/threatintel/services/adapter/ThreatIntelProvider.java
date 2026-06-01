package com.sdapro.threatintel.services.adapter;
import com.sdapro.threatintel.domain.reputation.ReputationResult;

// PATTERN: Adapter (Target Interface)
public interface ThreatIntelProvider {
    ReputationResult checkReputation(String indicator);
}
