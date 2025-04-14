package org.pe.llantatech.iam.service;

import org.pe.llantatech.iam.dto.LoginRequest;
import org.pe.llantatech.iam.dto.RegisterRequest;
import org.pe.llantatech.iam.entity.Role;
import org.pe.llantatech.iam.entity.User;
import org.pe.llantatech.iam.repository.RoleRepository;
import org.pe.llantatech.iam.repository.UserRepository;
import org.pe.llantatech.iam.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        if (encoder.matches(request.getPassword(), user.getPassword())) {
            return jwtUtil.generateToken(user);
        }
        throw new RuntimeException("Invalid credentials");
    }

    public String register(RegisterRequest request) {
        Role role = roleRepository.findByName("USER").orElseGet(() -> {
            Role r = new Role();
            r.setName("USER");
            return roleRepository.save(r);
        });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);
        return jwtUtil.generateToken(user);
    }
}
