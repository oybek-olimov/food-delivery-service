package org.example.deliveryservice.repository;

import org.example.deliveryservice.entity.auth.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AuthRoleRepository extends JpaRepository<AuthRole,Long> {

    Set<AuthRole> findAllById(Long userId);
}
