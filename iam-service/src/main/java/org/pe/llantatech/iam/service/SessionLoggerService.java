package org.pe.llantatech.iam.service;

import jakarta.servlet.http.HttpServletRequest;
import org.pe.llantatech.iam.entity.User;
import org.pe.llantatech.iam.entity.UserSession;
import org.pe.llantatech.iam.repository.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionLoggerService {

    @Autowired
    private UserSessionRepository sessionRepository;

    public void logSession(User user, String jwt, HttpServletRequest request) {
        UserSession session = new UserSession();
        session.setUsername(user.getUsername());
        session.setIpAddress(request.getRemoteAddr());
        session.setUserAgent(request.getHeader("User-Agent"));
        session.setJwt(jwt);
        session.setLoginTime(LocalDateTime.now());
        session.setActive(true);
        sessionRepository.save(session);
    }
}
