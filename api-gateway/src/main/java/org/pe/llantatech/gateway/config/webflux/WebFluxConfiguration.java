package org.pe.llantatech.gateway.config.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.WebFilter;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 4/14/25 @ 13:28
 */
public class WebFluxConfiguration {

    @Bean
    public WebFilter corsFilter() {
        return (exchange, chain) -> {
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "*");
            return chain.filter(exchange);
        };
    }

}
