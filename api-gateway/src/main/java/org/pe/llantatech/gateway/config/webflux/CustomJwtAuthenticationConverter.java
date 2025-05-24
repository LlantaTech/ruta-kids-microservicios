package org.pe.llantatech.gateway.config.webflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 5/24/25 @ 01:40
 */
@Slf4j
public class CustomJwtAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private final String clientId;

    public CustomJwtAuthenticationConverter(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        JwtGrantedAuthoritiesConverter realmConverter = new JwtGrantedAuthoritiesConverter();
        realmConverter.setAuthorityPrefix("ROLE_");
        realmConverter.setAuthoritiesClaimName("realm_access.roles");

        Collection<GrantedAuthority> realmAuthorities = realmConverter.convert(jwt);

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Set<GrantedAuthority> clientRoles = new HashSet<>();

        if (resourceAccess != null && resourceAccess.containsKey(clientId)) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(clientId);
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
    }
}