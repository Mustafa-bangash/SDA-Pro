package com.sdapro.audit.services;

public class AuditService {

    public void saveAuditLog(String event) {

        System.out.println(
            "Saving audit event: " + event
        );

    }
}
