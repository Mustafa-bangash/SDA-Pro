package com.sdapro.response.decorator;

public class AuditLogDecorator extends ResponseActionDecorator {
    
    public AuditLogDecorator(ResponseAction action) {
        super(action);
    }

    @Override
    public void execute() {
        System.out.println("[AUDIT LOG] Recording action attempt in the secure database...");
        super.execute();
        System.out.println("[AUDIT LOG] Action completed successfully. Record saved.");
    }
}
