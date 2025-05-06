package org.pe.llantatech.keycloakservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 4/28/25 @ 09:17
 */
@RestController
@RequestMapping("/api/v1/auth/hello")
public class HelloController {

    @GetMapping
    public String hello() {

        return "Hello World!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin')")
    public String helloAdmin() {
        return "Hello Admin!";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('parent')")
    public String helloUser() {
        return "Hello User!";
    }

    @GetMapping("/debug")
    public String debugRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        StringBuilder roles = new StringBuilder("User: " + username + " has roles: ");

        roles.append("Roles: " + authentication.getAuthorities().size() + " ");
        roles.append("Authorities: " + authentication.getDetails().toString());

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roles.append(authority.getAuthority()).append(" ");
        }

        return roles.toString();
    }
}
