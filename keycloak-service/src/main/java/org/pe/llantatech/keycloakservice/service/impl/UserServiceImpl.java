package org.pe.llantatech.keycloakservice.service.impl;

import lombok.AllArgsConstructor;
import org.pe.llantatech.keycloakservice.dto.LoginRequestDto;
import org.pe.llantatech.keycloakservice.dto.LoginResponseDto;
import org.pe.llantatech.keycloakservice.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 4/26/25 @ 16:48
 */
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Value("${app.keycloak.login.url}")
    private final String loginUrl;
    @Value("${app.keycloak.client-secret}")
    private final String clientSecret;
    @Value("${app.keycloak.grant-type}")
    private final String grantType;
    @Value("${app.keycloak.client-id}")
    private final String clientId;

    private final RestTemplate restTemplate;

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", request.username());
        map.add("password", request.password());
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", grantType);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);

        ResponseEntity<LoginResponseDto> loginResponse = restTemplate.postForEntity(loginUrl, httpEntity, LoginResponseDto.class);
        if (loginResponse.getStatusCode().is2xxSuccessful()) {
            return loginResponse.getBody();
        } else {
            throw new RuntimeException("Authentication failed: " + loginResponse.getStatusCode());
        }
    }
}
