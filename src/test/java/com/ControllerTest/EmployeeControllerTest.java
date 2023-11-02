package com.ControllerTest;

import com.entity.Employee;
import com.repository.EmployeeRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeControllerTest.class)
public class EmployeeControllerTest {


    @MockBean
    private EmployeeRepository employeeRepository;


    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testEmployeeController() throws Exception {
        List<Employee> list= new ArrayList<>();
        Employee employee2 = Employee.builder()
                .code(1236)
                .userName("Guptar1")
                .password("123")
                .email("nam@gmail.com")
                .build();

//                new Employee(1,1236, "Guptar2","123",null,"nam2@gmail.com",null,null,null,true,null,null,null,null,null);
//        Employee employee3 = new Employee(2,1238, "Guptar1","123",null,"nam1@gmail.com",null,null,null,true,null,null,null,null,null);
//        Employee employee = new Employee(3,1237, "Guptar3","123",null,"nam4@gmail.com",null,null,null,true,null,null,null,null,null);
//        list.add(employee);
//        list.add(employee2);
//        list.add(employee3);
        list.add(employee2);
        Mockito.when(employeeRepository.findAll()).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/getEmployee"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }
}
