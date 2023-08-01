package com.service;

import com.dto.EmCoDTO;
import com.dto.EmployeeDTO;
import com.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    Employee findByUserName(String un);
    List<Employee> findAll();
    boolean existsByUserName(String un);
    boolean existsByEmail(String email);
    Employee saveOrUpdate(Employee employee);
    EmployeeDTO UpdateEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO SaveEmployee(EmployeeDTO employeeDTO);
    Employee findByCode(int id);
    Employee findByEmployeeId(int id);
    void deleteByEmployeeId(int id);
    List<Employee> listNotCheckOut();
    List<Employee> listNotCheckIn();
    List<Employee> findByUserNameASC(String username);
    List<Employee> listEmployeeIOwithTime(String dateStart, String dateEnd);
    int createCode();
    Page<EmCoDTO> findEmCoDTo(String username, Pageable pageable);

    List<EmployeeDTO> searchEmployeesByProject(String code);

}
