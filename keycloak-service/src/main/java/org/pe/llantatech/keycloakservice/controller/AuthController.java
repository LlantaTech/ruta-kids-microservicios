package org.pe.llantatech.keycloakservice.controller;

import lombok.AllArgsConstructor;
import org.pe.llantatech.keycloakservice.dto.LoginRequestDto;
import org.pe.llantatech.keycloakservice.dto.LoginResponseDto;
import org.pe.llantatech.keycloakservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 4/26/25 @ 16:53
 */
@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        LoginResponseDto loginResponse = userService.login(requestDto);
        return ResponseEntity.ok(loginResponse);
    }
}
