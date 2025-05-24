package org.pe.llantatech.notifications.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pe.llantatech.notifications.model.PushNotification;
import org.pe.llantatech.notifications.service.WebSocketService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 5/24/25 @ 00:21
 */
@Service
@AllArgsConstructor
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotification(String topic, PushNotification notification) {
        messagingTemplate.convertAndSend(topic, notification);
        log.info("Sent notification to topic {}", topic);
    }

    @Override
    public void sendNotificationToUser(String userId, PushNotification notification) {
        String destination = "/topic/notifications/user/" + userId;
        messagingTemplate.convertAndSend(destination, notification);
        log.info("Sent notification to user {} at destination {}", userId, destination);
    }
}
