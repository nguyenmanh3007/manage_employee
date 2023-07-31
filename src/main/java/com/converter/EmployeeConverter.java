package com.converter;

import com.dto.EmployeeDTO;
import com.entity.Employee;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmployeeConverter {

    private final PasswordEncoder encoder;

    public Employee toEntity(Employee dto, EmployeeDTO employee){
        dto.setCode(employee.getCode());
        dto.setUserName(employee.getUserName());
        dto.setPassword(encoder.encode(employee.getPassword()));
        dto.setCreated(employee.getCreated());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setTimeCheckin(employee.getTimeCheckin());
        dto.setTimeCheckout(employee.getTimeCheckout());
        dto.setEmployeeStatus(employee.isEmployeeStatus());
        dto.setListRoles(employee.getListRoles());
        return dto;
    }
}
