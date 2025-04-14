package org.pe.llantatech.user.service;

import org.pe.llantatech.user.entity.User;
import org.pe.llantatech.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public User createUser(User user) {
        User saved = userRepository.save(user);
        try {
            String json = objectMapper.writeValueAsString(saved);
            kafkaTemplate.send("user-created", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saved;
    }
}
