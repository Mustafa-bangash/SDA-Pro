package com.sdapro.response.decorator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// PATTERN: Decorator
// RATIONALE: Some response actions must be reversible (e.g. a blocked IP
// that turns out to be legitimate, or an isolated endpoint that belongs to
// an executive). RollbackCapabilityDecorator wraps any ResponseAction,
// captures a snapshot before execution, and enables rollback without
// modifying any base action class.
public class RollbackCapabilityDecorator extends ResponseActionDecorator {

    // Snapshot store for rollback
    private final List<RollbackSnapshot> snapshots = new ArrayList<>();

    public RollbackCapabilityDecorator(ResponseAction wrappedAction) {
        super(wrappedAction);
    }

    @Override
    public String execute(String target) {
        // PATTERN: Decorator — capture snapshot before delegating
        captureSnapshot(target);
        System.out.println("[RollbackDecorator] Snapshot captured for target=" + target);

        String outcome = wrappedAction.execute(target);

        System.out.println("[RollbackDecorator] Action completed with outcome=" + outcome
                + " — rollback available");
        return outcome;
    }

    // Captures pre-execution state for rollback
    private void captureSnapshot(String target) {
        RollbackSnapshot snapshot = new RollbackSnapshot();
        snapshot.target = target;
        snapshot.actionType = wrappedAction.getActionType();
        snapshot.capturedAt = Instant.now().toString();
        snapshot.preState = "ACTIVE"; // Real impl: query actual asset state
        snapshots.add(snapshot);
    }

    // Rollback the last executed action
    public String rollback() {
        if (snapshots.isEmpty()) {
            return "NO_SNAPSHOT_AVAILABLE";
        }
        RollbackSnapshot last = snapshots.get(snapshots.size() - 1);
        System.out.println("[RollbackDecorator] Rolling back action=" + last.actionType
                + " on target=" + last.target
                + " restoring to preState=" + last.preState);
        snapshots.remove(last);
        return "ROLLED_BACK";
    }

    public List<RollbackSnapshot> getSnapshots() {
        return new ArrayList<>(snapshots);
    }

    @Override
    public String getActionType() {
        return "ROLLBACK[" + wrappedAction.getActionType() + "]";
    }

    // Snapshot data structure
    public static class RollbackSnapshot {
        public String target;
        public String actionType;
        public String capturedAt;
        public String preState;
    }
}
