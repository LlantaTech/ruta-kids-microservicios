package org.pe.llantatech.notifications.pipeline.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pe.llantatech.notifications.model.PushNotificationType;

import java.util.Objects;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 5/24/25 @ 00:37
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class KafkaPushNotificationPayload {
    private String userId;
    private String title;
    private String message;
    private PushNotificationType type;
    private boolean broadcast;
}