package com.sdapro.alertingestion.domain.alert;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

// PATTERN: Composite
// RATIONALE: Both single alerts and grouped alerts (campaigns, clusters)
// must be treated uniformly for enrichment and response.
public interface AlertComponent {
    UUID getId();
    Severity getSeverity();
    Instant getTimestamp();
    void add(AlertComponent component);
    void remove(AlertComponent component);
    List<AlertComponent> getChildren();
    boolean isComposite();
}
