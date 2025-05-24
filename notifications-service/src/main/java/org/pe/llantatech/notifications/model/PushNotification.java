package org.pe.llantatech.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 5/23/25 @ 22:46
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class PushNotification {
    private  String title;
    private  String message;
    private  PushNotificationType type;


}
