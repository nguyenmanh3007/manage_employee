package com.ControllerTest;


import com.dto.EmployeeDTO;
import com.entity.Employee;
import com.repository.EmployeeRepository;
import com.service.EmployeeService;
import com.service.impl.EmployeeServiceImpl;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testEmployeeController() throws Exception {
        List<EmployeeDTO> list= new ArrayList<>();
        EmployeeDTO employee2 = new EmployeeDTO(1,1236, "Guptar2","123",null,"nam2@gmail.com",null,null,null,true,null,null,null,null,null);
        EmployeeDTO employee3 = new EmployeeDTO(2,1238, "Guptar1","123",null,"nam1@gmail.com",null,null,null,true,null,null,null,null,null);
        EmployeeDTO employee = new EmployeeDTO(3,1237, "Guptar3","123",null,"nam4@gmail.com",null,null,null,true,null,null,null,null,null);
        list.add(employee);
        list.add(employee2);
        list.add(employee3);
        Mockito.when(employeeService.findAll()).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/getEmployee"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }
}
