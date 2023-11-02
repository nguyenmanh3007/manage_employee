package com.ServiceTest;


import com.dto.EmployeeDTO;
import com.entity.ERole;
import com.entity.Employee;
import com.entity.Roles;
import com.mapper.EmployeeMapper;
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
import java.util.stream.Collectors;


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
        Employee employee = Employee.builder()
                .code(1236)
                .userName("Guptar1")
                .password("123")
                .email("nam@gmail.com")
                .build();

        Mockito.when(employeeService.findByUserName("Guptar")).thenReturn(employee);

        Employee result = employeeService.findByUserName("Guptar");
        Assertions.assertEquals(employee, result);
    }
    @Test
    public void testfindAll() {

        List<Employee> list = new ArrayList<>();
        Employee employee1 = Employee.builder()
                .code(1236)
                .userName("Guptar1")
                .password("123")
                .email("nam@gmail.com")
                .build();
        Employee employee2 = Employee.builder()
                        .code(1237)
                        .userName("Guptar2")
                        .password("123")
                        .email("nam2@gmail.com")
                        .build();
        Employee employee3 = Employee.builder()
                .code(1238)
                .userName("Guptar3")
                .password("123")
                .email("nam3@gmail.com")
                .build();
        list.add(employee1);
        list.add(employee2);
        list.add(employee3);
        Mockito.when(employeeService.findAll()).thenReturn(list.stream()
                .map(employee -> EmployeeMapper.MAPPER.employeeToEmployeeDto(employee))
                .collect(Collectors.toList()));
        //test
        List<EmployeeDTO> empList = employeeService.findAll();
        Assertions.assertEquals(list.size(), empList.size());
    }
    @Test
    public void updateEmployee() {
        EmployeeDTO employee = EmployeeDTO.builder()
                .employeeId(93)
                .code(1236)
                .userName("Guptar1")
                .password("123")
                .email("nam@gmail.com")
                .build();
        try {
            Mockito.when(employeeService.UpdateEmployee(employee)).thenReturn(employee);
        }
        catch (NullPointerException e){
            System.out.println("not roles");
        }
        Mockito.when(employeeService.findByUserName("Guptar1")).thenReturn(EmployeeMapper.MAPPER.employeeDtoToEmployee(employee));

        //test
        Employee result = employeeService.findByUserName("Guptar1");
        Assertions.assertEquals(employee ,EmployeeMapper.MAPPER.employeeToEmployeeDto(result));
    }
}
