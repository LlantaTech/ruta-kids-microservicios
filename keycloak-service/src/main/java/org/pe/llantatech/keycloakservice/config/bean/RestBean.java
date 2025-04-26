package org.pe.llantatech.keycloakservice.config.bean;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 4/23/25 @ 21:36
 */
@Configuration
public class RestBean {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
