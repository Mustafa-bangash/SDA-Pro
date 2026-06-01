package com.sdapro.response.decorator;

// PATTERN: Decorator
// RATIONALE: We want to add audit logging dynamically to any response action without modifying the core action code.
public abstract class ResponseActionDecorator implements ResponseAction {
    protected ResponseAction wrappedAction;

    public ResponseActionDecorator(ResponseAction action) {
        this.wrappedAction = action;
    }

    public void execute() {
        wrappedAction.execute();
    }
}
