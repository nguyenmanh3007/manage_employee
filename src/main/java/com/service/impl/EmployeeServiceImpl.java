package com.service.impl;

import com.converter.EmployeeConverter;
import com.dto.EmCoDTO;
import com.dto.EmployeeDTO;
import com.entity.ERole;
import com.entity.Employee;
import com.entity.Roles;
import com.repository.ConfirmRepository;
import com.repository.EmployeeRepository;
import com.service.EmployeeService;
import com.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ConfirmRepository confirmRepository;
    @Autowired
    private EmployeeConverter employeeConverter;

    @Autowired
    private RoleService roleService;
    @Override
    public Employee findByUserName(String un) {
        return employeeRepository.findByUserName(un);
    }

    @Override
    public boolean existsByUserName(String un) {
        return employeeRepository.existsByUserName(un);
    }

    @Override
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Override
    public Employee saveOrUpdate(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public EmployeeDTO UpdateEmployee(EmployeeDTO employeeDTO) {
        Employee newEmployee= new Employee();
        Employee oldEmployee=  findByEmployeeId(employeeDTO.getEmployeeId());
        employeeDTO.setCreated(oldEmployee.getCreated());
        employeeDTO.setCode(oldEmployee.getCode());
        employeeDTO.setEmployeeStatus(oldEmployee.isEmployeeStatus());
        Set<String> strRoles = employeeDTO.getListRole();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles.size()==0) {
            // User quyen mac dinh
            Roles employeeRole = roleService.findByRoleName(ERole.ROLE_EMPLOYEE)
                    .orElseThrow(() -> new RuntimeException("Error: Employee is not found"));
            listRoles.add(employeeRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Roles adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(adminRole);
                        break;
                    case "employee":
                        Roles employeeRole = roleService.findByRoleName(ERole.ROLE_EMPLOYEE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(employeeRole);
                        break;
                }
            });
        }
        employeeDTO.setListRoles(listRoles);
        newEmployee= employeeConverter.toEntity(oldEmployee,employeeDTO);
        return employeeConverter.toDTO(employeeRepository.save(newEmployee));
    }

    @Override
    public EmployeeDTO SaveEmployee(EmployeeDTO employeeDTO) {
        Employee newEmployee= new Employee();
        newEmployee= employeeConverter.toEntity(employeeDTO);
        return employeeConverter.toDTO(employeeRepository.save(newEmployee));
    }


    @Override
    public Employee findByCode(int id) {
        List<Employee> list= employeeRepository.findAll();
        for(Employee employee:list) {
            if (employee.getCode() == id) {
                return employee;
            }
        }
        return null;
    }

    @Override
    public Employee findByEmployeeId(int id) {
        List<Employee> list= employeeRepository.findAll();
        for(Employee employee:list) {
            if (employee.getEmployeeId() == id) {
                return employee;
            }
        }
        return null;
    }


    @Override
    public void deleteByEmployeeId(int id) {
        employeeRepository.deleteRoleEmployee(id);
//        confirmRepository.deleteByEmployeeId(id);
        employeeRepository.deleteByEmployeeId(id);
    }

    @Override
    public List<Employee> listNotCheckOut() {
        Date now= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateNow=sdf.format(now);
        return employeeRepository.listNotCheckOut(dateNow);
    }

    @Override
    public List<Employee> listNotCheckIn() {
        Date now= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateNow=sdf.format(now);
        return employeeRepository.listNotCheckIn(dateNow);
    }

    @Override
    public List<Employee> findByUserNameASC(String username) {
        return employeeRepository.findByUserNameASC(username);
    }

    @Override
    public List<Employee> listEmployeeIOwithTime(String dateStart, String dateEnd) {
        return employeeRepository.listEmployeeIOwithTime(dateStart,dateEnd);
    }

    @Override
    public int createCode() {
        Random random = new Random();
        int min = 1000;
        int max = 9999;
        return random.nextInt(max - min + 1) + min;
    }

    @Override
    public Page<EmCoDTO> findEmCoDTo(String username, Pageable pageable) {
        return employeeRepository.findEmCoDTo(username,pageable);
    }

}
