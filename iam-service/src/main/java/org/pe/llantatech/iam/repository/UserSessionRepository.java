package org.pe.llantatech.iam.repository;

import org.pe.llantatech.iam.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
}
