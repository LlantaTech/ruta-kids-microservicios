package org.pe.llantatech.audit.listener;

import org.pe.llantatech.audit.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaAuditListener {

    @Autowired
    private AuditService auditService;

    @KafkaListener(topics = "user-created", groupId = "audit-service-group")
    public void listenUserCreated(String message) {
        auditService.logEvent("USER_CREATED", "user-service", message);
    }

    @KafkaListener(topics = "user-logged-in", groupId = "audit-service-group")
    public void listenUserLoggedIn(String message) {
        auditService.logEvent("USER_LOGGED_IN", "iam-service", message);
    }
}
