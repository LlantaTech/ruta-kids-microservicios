package org.pe.llantatech.gateway.config.webflux;

import org.pe.llantatech.gateway.config.app.KeycloakProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(KeycloakProperties.class)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private final KeycloakProperties keycloak;

    public SecurityConfig(KeycloakProperties keycloak) {
        this.keycloak = keycloak;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/public/**").permitAll()
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(unauthorizedEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
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

    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        return jwt -> {
            JwtGrantedAuthoritiesConverter realmConverter = new JwtGrantedAuthoritiesConverter();
            realmConverter.setAuthorityPrefix("ROLE_");
            realmConverter.setAuthoritiesClaimName("realm_access.roles");

            Collection<GrantedAuthority> realmAuthorities = realmConverter.convert(jwt);

            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            Set<GrantedAuthority> clientRoles = new HashSet<>();

            if (resourceAccess != null && resourceAccess.containsKey(keycloak.getClientId())) {
                Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(keycloak.getClientId());
                if (clientAccess != null) {
                    Collection<String> roles = (Collection<String>) clientAccess.get("roles");
                    clientRoles.addAll(
                            roles.stream()
                                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                    .toList()
                    );
                }
            }

            Collection<GrantedAuthority> allAuthorities = Stream.concat(
                    realmAuthorities.stream(),
                    clientRoles.stream()
            ).toList();

            String principal = jwt.getClaimAsString("preferred_username");
            if (principal == null) {
                principal = jwt.getSubject();
            }

            log.info("🔐 Principal: {}", principal);
            log.info("🎭 Authorities: {}", allAuthorities);

            return Mono.just(new JwtAuthenticationToken(jwt, allAuthorities, principal));
        };
    }


    @Bean
    public ServerAuthenticationEntryPoint unauthorizedEntryPoint() {
        return (exchange, ex) -> {
            String message = "Unauthorized. Missing or invalid token.";
            // Si es BadCredentialsException usamos su mensaje
            if (ex instanceof BadCredentialsException) {
                message = ex.getMessage();
            } else if (ex.getCause() instanceof BadCredentialsException) {
                message = ex.getCause().getMessage();
            }

            String body = String.format("{\"error\": \"%s\"}", message);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
            );
        };
    }

    @Bean
    public ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, denied) -> {
            String body = "{\"error\": \"Access denied. You do not have permission to access this resource.\"}";
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            exchange.getResponse().getHeaders().add("Content-Type", "application/json");
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                    .bufferFactory().wrap(bytes)));
        };
    }
}

