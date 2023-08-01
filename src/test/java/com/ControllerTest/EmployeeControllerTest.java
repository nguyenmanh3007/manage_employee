package com.ControllerTest;


import com.entity.Employee;
import com.repository.EmployeeRepository;
import com.service.EmployeeService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class EmployeeControllerTest {

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testEmployeeController() throws Exception {
        List<Employee> list= new ArrayList<>();
        Employee employee2 = new Employee(1237, "Guptar2","123","nam2@gmail.com",null);
        Employee employee3 = new Employee(1238, "Guptar3","123","nam3@gmail.com",null);
        Employee employee = new Employee(1236, "Guptar","123","nam@gmail.com",null);
        list.add(employee);
        list.add(employee2);
        list.add(employee3);
        Mockito.when(employeeRepository.findAll()).thenReturn(list);
        List<Employee> result= employeeService.findAll();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/getEmployee"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }
}
