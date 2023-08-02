package com.service;

import com.entity.ERole;
import com.entity.Roles;

import java.util.Optional;
import java.util.Set;

public interface RoleService {
    Optional<Roles> findByRoleName(ERole roleName);

    Set<Roles> getRole(Set<String> strRoles);
}
