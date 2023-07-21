package com.converter;

import com.dto.EmployeeDTO;
import com.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConverter {

    @Autowired
    private PasswordEncoder encoder;
    public EmployeeDTO toDTO(Employee employee){
        EmployeeDTO dto= new EmployeeDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setCode(employee.getCode());
        dto.setUserName(employee.getUserName());
        dto.setPassword(employee.getPassword());
        dto.setCreated(employee.getCreated());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setTimeCheckin(employee.getTimeCheckin());
        dto.setTimeCheckout(employee.getTimeCheckout());
        dto.setEmployeeStatus(employee.isEmployeeStatus());
        dto.setListRoles(employee.getListRoles());
        return dto;
    }
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
    public Employee toEntity(EmployeeDTO employee){
        Employee dto = new Employee();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setCode(employee.getCode());
        dto.setUserName(employee.getUserName());
        dto.setPassword(employee.getPassword());
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
