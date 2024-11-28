package org.example.deliveryservice.repository;

import org.example.deliveryservice.entity.auth.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AuthUserRepository extends JpaRepository<AuthUser,Long> {

    Optional<AuthUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
