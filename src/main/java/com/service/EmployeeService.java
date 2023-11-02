package com.service;

import com.dto.EmCoDTO;
import com.dto.EmployeeDTO;
import com.entity.Confirm;
import com.entity.Employee;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface EmployeeService {

    Employee findByUserName(String username);

    @Transactional
    void saveEmployee(Employee employee);
    List<EmployeeDTO> findAll();
    boolean existsByUserName(String un);
    boolean existsByEmail(String email);
    EmployeeDTO saveOrUpdate(EmployeeDTO employeeDTO);
    EmployeeDTO UpdateEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO SaveEmployee(EmployeeDTO employeeDTO);
    Employee findByCode(int id);
    Employee findByEmployeeId(int id);
    void deleteByEmployeeId(int id);
    List<EmployeeDTO> listNotCheckOut();
    List<EmployeeDTO> listNotCheckIn();
    List<EmployeeDTO> findByUserNameASC(String username);
    List<EmployeeDTO> listEmployeeIOwithTime(String dateStart, String dateEnd);
    int createCode();

    Confirm employeeCheckInOut(int code);
    Page<EmCoDTO> findEmCoDTo(String username, Pageable pageable);
    String[] getWeekAtNow();
    List<EmployeeDTO> searchEmployeesByProject(String code);

}
