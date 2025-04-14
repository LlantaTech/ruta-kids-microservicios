package org.pe.llantatech.iam.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String ipAddress;
    private String userAgent;
    private String jwt;
    private LocalDateTime loginTime;
    private boolean active;

    // Getters and setters
}
