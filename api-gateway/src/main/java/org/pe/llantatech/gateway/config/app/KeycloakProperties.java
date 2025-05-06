package org.pe.llantatech.gateway.config.app;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 06/05/25 @ 16:47
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.keycloak")
public class KeycloakProperties {

    private String clientId;
    private String issuerUri;
    private String jwkSetUri;
}