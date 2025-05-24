package org.pe.llantatech.notifications.service;

import org.pe.llantatech.notifications.model.PushNotification;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 5/24/25 @ 00:20
 */
public interface WebSocketService {

    void sendNotification(String topic, PushNotification notification);

    void sendNotificationToUser(String userId, PushNotification notification);

}
