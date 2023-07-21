package com.Controller;

import com.entity.Confirm;
import com.entity.Employee;
import com.repository.ConfirmRepository;
import com.repository.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class TestController {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ConfirmRepository confirmRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void testfindEmployee(){
        Employee employee = new Employee();
        employee.setUserName("john.doe");
        employeeRepository.save(employee);
        Mockito.verify(employeeRepository).save(employee);
    }
    @Test
    public void testSaveEmployee(){
        Employee employee= new Employee();
        employee.setCode(3342);
        employee.setUserName("asss");
        employee.setPassword("123");
        employee.setEmail("nguy@gmail.com");

        Confirm confirm1= new Confirm();
        confirm1.setEmployee(employee);
        Confirm confirm2= new Confirm();
        confirm2.setEmployee(employee);

        employee.getConfirms().add(confirm1);
        employee.getConfirms().add(confirm2);


        int n=confirmRepository.findAll().size();
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);

        Employee savedEmployee = employeeRepository.save(employee);
        int m = confirmRepository.findAll().size();
        System.out.println(n +" "+m);
        assertNotNull(savedEmployee);
        assertEquals(n+2,m);
        assertEquals(employee.getUserName(), savedEmployee.getUserName());
        assertEquals(employee.getCode(), savedEmployee.getCode());
        assertEquals(employee.getEmail(), savedEmployee.getEmail());
    }
}
