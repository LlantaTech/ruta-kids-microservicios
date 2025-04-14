package org.pe.llantatech.audit.listener;

import org.pe.llantatech.audit.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ParentKafkaListener {

    @Autowired
    private AuditService auditService;

    @KafkaListener(topics = "parent-created", groupId = "audit-service-group")
    public void listen(String message) {
        auditService.logEvent("PARENT_CREATED", "parent-management-service", message);
    }
}
