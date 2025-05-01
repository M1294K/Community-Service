package com.community.repository;

import com.community.model.User;
import com.community.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByRole(Role role);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findOptionalByEmail(String email);
}
