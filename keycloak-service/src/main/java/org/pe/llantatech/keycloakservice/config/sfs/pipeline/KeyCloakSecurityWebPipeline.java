package org.pe.llantatech.keycloakservice.config.sfs.pipeline;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.pe.llantatech.keycloakservice.config.jwt.converter.JwtConverter;
import org.pe.llantatech.keycloakservice.handler.CustomKeycloakAuthenticationHandler;
import org.pe.llantatech.keycloakservice.handler.RestAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 4/28/25 @ 08:26
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class KeyCloakSecurityWebPipeline {

    private final JwtConverter jwtAuthConverter;

    private final RestAccessDeniedHandler restAccessDeniedHandler;

    private final CustomKeycloakAuthenticationHandler customKeycloakAuthenticationHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/hello/**").permitAll()
                        .requestMatchers("/api/v1/auth/login/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        .anyRequest()
                        .authenticated()
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthConverter)
                        )
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(STATELESS)
                )

                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(restAccessDeniedHandler);
                })
        ;

        return http.build();
    }

}