package org.pe.llantatech.gateway.config.sfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 4/14/25 @ 13:44
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/**", "/auth/**").permitAll() // Permitidos sin login
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtSpec -> jwtSpec.jwtDecoder(customJwtDecoder()))
                )
                .build();
    }

    @Bean
    public ReactiveJwtDecoder customJwtDecoder() {
        NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder
                .withJwkSetUri("http://localhost:8081/realms/your-realm/protocol/openid-connect/certs")
                .build();

        jwtDecoder.setJwtValidator(JwtValidators.createDefault());

        return token -> jwtDecoder.decode(token).map(jwt -> {
            log.info("🔐 JWT recibido: subject={}, roles={}, issuedAt={}, expiresAt={}",
                    jwt.getSubject(),
                    jwt.getClaimAsString("roles"),
                    jwt.getIssuedAt(),
                    jwt.getExpiresAt()
            );
            return jwt;
        });
    }
}
