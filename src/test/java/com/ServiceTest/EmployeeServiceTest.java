package com.ServiceTest;


import com.entity.Employee;
import com.repository.EmployeeRepository;
import com.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testfindByUserName() {
        Employee employee = new Employee(1236, "Guptar","123","nam@gmail.com");

        Mockito.when(employeeRepository.findByUserName("Guptar")).thenReturn(employee);

        Employee result = employeeService.findByUserName("Guptar");
        Assertions.assertEquals(employee, result);
    }
    @Test
    public void testfindAll() {

        List<Employee> list = new ArrayList<>();
        Employee employee1 = new Employee(1236, "Guptar1","123","nam1@gmail.com");
        Employee employee2 = new Employee(1237, "Guptar2","123","nam2@gmail.com");
        Employee employee3 = new Employee(1238, "Guptar3","123","nam3@gmail.com");
        list.add(employee1);
        list.add(employee2);
        list.add(employee3);
        Mockito.when(employeeRepository.findAll()).thenReturn(list);

        //test
        List<Employee> empList = employeeService.findAll();
        assertEquals(list.size(), empList.size());
    }
}
