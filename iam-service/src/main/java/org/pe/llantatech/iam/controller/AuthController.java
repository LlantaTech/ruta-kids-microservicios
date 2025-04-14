package org.pe.llantatech.iam.controller;

import org.pe.llantatech.iam.dto.LoginRequest;
import org.pe.llantatech.iam.dto.RegisterRequest;
import org.pe.llantatech.iam.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private SessionLoggerService sessionLoggerService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String token = authService.login(request);
        sessionLoggerService.logSession(authService.getUser(request.getUsername()), token, httpRequest);
        return token;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }
}
