package com.repository;

import com.entity.ERole;
import com.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByRoleName(ERole roleName);
}
