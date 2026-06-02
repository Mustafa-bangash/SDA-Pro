package com.sdapro.response.decorator;

// PATTERN: Decorator
// RATIONALE: High-impact response actions (isolate endpoint, disable user account)
// must require analyst approval before execution. ApprovalGateDecorator wraps
// any ResponseAction and adds an approval check dynamically without modifying
// the base action class. This satisfies the requirement that "response actions
// are decorated with logging, approval gates, and rollback capabilities."
public class ApprovalGateDecorator extends ResponseActionDecorator {

    private final String requiredApprovalLevel; // ANALYST, SENIOR_ANALYST, MANAGER
    private boolean autoApproveForLowRisk;

    public ApprovalGateDecorator(ResponseAction wrappedAction,
                                  String requiredApprovalLevel,
                                  boolean autoApproveForLowRisk) {
        super(wrappedAction);
        this.requiredApprovalLevel = requiredApprovalLevel;
        this.autoApproveForLowRisk = autoApproveForLowRisk;
    }

    public ApprovalGateDecorator(ResponseAction wrappedAction) {
        super(wrappedAction);
        this.requiredApprovalLevel = "ANALYST";
        this.autoApproveForLowRisk = true;
    }

    @Override
    public String execute(String target) {
        // PATTERN: Decorator — add approval gate before delegating to wrapped action
        System.out.println("[ApprovalGate] Checking approval for action on target=" + target
                + " requiredLevel=" + requiredApprovalLevel);

        boolean approved = checkApproval(target);

        if (!approved) {
            System.out.println("[ApprovalGate] Action BLOCKED — awaiting " + requiredApprovalLevel + " approval");
            return "BLOCKED_PENDING_APPROVAL";
        }

        System.out.println("[ApprovalGate] Approval GRANTED — delegating to wrapped action");
        // Delegate to the next decorator or base action
        return wrappedAction.execute(target);
    }

    private boolean checkApproval(String target) {
        // Real impl: query ApprovalService via REST, check pending approvals table
        // Simulation: auto-approve for non-critical targets, block for production assets
        if (autoApproveForLowRisk && !target.contains("prod")) {
            System.out.println("[ApprovalGate] Auto-approved (low risk target)");
            return true;
        }
        if (target.contains("prod")) {
            System.out.println("[ApprovalGate] Production asset — requires " + requiredApprovalLevel + " sign-off");
            // Simulate senior analyst pre-approved
            return requiredApprovalLevel.equals("ANALYST") || requiredApprovalLevel.equals("SENIOR_ANALYST");
        }
        return true;
    }

    @Override
    public String getActionType() {
        return "APPROVAL_GATE[" + wrappedAction.getActionType() + "]";
    }
}
