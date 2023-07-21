package com.service;

import com.dto.EmployeeDTO;
import com.dto.EmployeeWithConfirmDTO;
import com.entity.Confirm;
import com.entity.Employee;

import java.util.List;

public interface EmployeeService {
    Employee findByUserName(String un);
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
}
