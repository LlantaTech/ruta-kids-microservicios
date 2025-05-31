package org.pe.llantatech.gateway.config.webflux;

import org.pe.llantatech.gateway.config.app.KeycloakProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

@Configuration
@EnableConfigurationProperties(KeycloakProperties.class)
public class JwtDecoderConfig {

    private static final Logger log = LoggerFactory.getLogger(JwtDecoderConfig.class);

    private final KeycloakProperties keycloak;

    public JwtDecoderConfig(KeycloakProperties keycloak) {
        this.keycloak = keycloak;
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder(@Value("${app.keycloak.jwk-set-uri}") String jwkSetUri) {
        NimbusReactiveJwtDecoder decoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();

        return token -> decoder.decode(token)
                .onErrorResume(e -> {
                    log.error("❌ Invalid JWT: {}", e.getMessage());
                    return Mono.error(new BadCredentialsException("Invalid or expired JWT"));
                });
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> customJwtAuthenticationConverter() {
        return new CustomJwtAuthenticationConverter(keycloak.getClientId());
    }

}

