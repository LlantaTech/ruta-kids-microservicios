package org.pe.llantatech.audit.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String source;
    private String payload;
    private LocalDateTime timestamp;

    // Getters and setters
}
