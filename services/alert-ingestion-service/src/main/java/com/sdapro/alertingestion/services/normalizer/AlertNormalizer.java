package com.sdapro.alertingestion.services.normalizer;

import com.sdapro.alertingestion.domain.alert.CanonicalAlert;
import com.sdapro.alertingestion.domain.source.AlertSourceType;

// PATTERN: Factory Method (Product Interface)
// RATIONALE: Defines the contract that all normalizers must follow.
// Factory Method returns this interface so client code stays decoupled
// from specific normalizer implementations.
public interface AlertNormalizer {
    CanonicalAlert normalize(String rawPayload);
    boolean supports(AlertSourceType sourceType);
}
