package org.pe.llantatech.user.repository;

import org.pe.llantatech.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
