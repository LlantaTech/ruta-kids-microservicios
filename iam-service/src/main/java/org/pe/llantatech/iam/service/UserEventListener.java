package org.pe.llantatech.iam.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListener {

    @KafkaListener(topics = "user-created", groupId = "iam-service-group")
    public void handleUserCreated(String userJson) {
        System.out.println("User created event received in IAM: " + userJson);
        // Aquí podrías sincronizar permisos, enviar notificaciones, etc.
    }
}
