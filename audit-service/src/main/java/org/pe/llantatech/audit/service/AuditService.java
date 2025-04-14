package org.pe.llantatech.audit.service;

import org.pe.llantatech.audit.entity.AuditLog;
import org.pe.llantatech.audit.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository repository;

    public void logEvent(String type, String source, String payload) {
        AuditLog log = new AuditLog();
        log.setType(type);
        log.setSource(source);
        log.setPayload(payload);
        log.setTimestamp(LocalDateTime.now());
        repository.save(log);
    }
}
