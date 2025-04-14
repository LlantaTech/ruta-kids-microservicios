package org.pe.llantatech.iam.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.pe.llantatech.iam.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationMs}")
    private int expirationMs;

    public String generateToken(User user) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        String roles = user.getRoles().stream()
                .map(r -> r.getName())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
