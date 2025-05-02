package org.pe.llantatech.keycloakservice.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: ruta-kids-microservicios
 * Date: 4/28/25 @ 08:34
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 403 Forbidden
        httpServletResponse.getOutputStream().println("{ \"timestamp\": \"" + LocalDateTime.now() + "\", \"error\": \"" + "Forbidden" + "\", \"status\": 403 , \"message\": \"" + e.getMessage() + "\", \"path\": \"" + httpServletRequest.getRequestURI() + "\" }");
    }
}
