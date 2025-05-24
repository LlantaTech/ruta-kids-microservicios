package org.pe.llantatech.notifications.pipeline.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pe.llantatech.notifications.model.PushNotification;
import org.pe.llantatech.notifications.pipeline.payload.KafkaPushNotificationPayload;
import org.pe.llantatech.notifications.service.WebSocketService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 5/24/25 @ 00:19
 */
@Slf4j
@Component
@AllArgsConstructor
public class NotificationEventListener {

    private final WebSocketService webSocketService;


    @KafkaListener(topics = "notifications", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(KafkaPushNotificationPayload payload) {
        PushNotification notification = new PushNotification(
                payload.getTitle(),
                payload.getMessage(),
                payload.getType()
        );

        if (payload.isBroadcast()) {
            webSocketService.sendNotification("/topic/notifications/general", notification);
        } else {
            webSocketService.sendNotificationToUser(payload.getUserId(), notification);
        }

    }
}
