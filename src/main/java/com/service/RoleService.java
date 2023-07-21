package com.service;

import com.entity.ERole;
import com.entity.Roles;

import java.util.Optional;

public interface RoleService {
    Optional<Roles> findByRoleName(ERole roleName);
}
