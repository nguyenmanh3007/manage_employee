package com.service.impl;

import com.entity.ERole;
import com.entity.Roles;
import com.repository.RoleRepository;
import com.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Optional<Roles> findByRoleName(ERole roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public Set<Roles> getRole(Set<String> strRoles) {
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles == null) {
            Roles userRole = roleRepository.findByRoleName(ERole.ROLE_EMPLOYEE)
                    .orElseThrow(() -> new RuntimeException("Error: Employee role is not found"))
                    ;
            listRoles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Roles adminRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(adminRole);
                        break;
                    case "employee":
                        Roles modRole = roleRepository.findByRoleName(ERole.ROLE_EMPLOYEE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(modRole);
                        break;
                }
            });
        }
        return listRoles;
    }
}
