package com.ServiceTest;


import com.entity.Confirm;
import com.entity.Employee;
import com.repository.ConfirmRepository;
import com.service.ConfirmService;
import com.service.impl.ConfirmServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class ConfirmServiceTest {

    @Autowired
    private ConfirmServiceImpl confirmService;

    @MockBean
    private ConfirmRepository confirmRepository;

    @Test
    public void testfindAll() {
        List<Confirm> list = new ArrayList<>();
        Employee employee1 = Employee.builder()
                .code(1236)
                .userName("Guptar1")
                .password("123")
                .email("nam1@gmail.com")
                .phone("094324324")
                .build();
        Employee employee2 = Employee.builder()
                .code(1237)
                .userName("Guptar2")
                .password("123")
                .email("nam2@gmail.com")
                .phone("094324453")
                .build();
        Employee employee3 = Employee.builder()
                .code(1238)
                .userName("Guptar3")
                .password("123")
                .email("nam3@gmail.com")
                .phone("0943247686")
                .build();
        Confirm confirm1 = new Confirm(employee1);
        Confirm confirm2 = new Confirm(employee2);
        Confirm confirm3 = new Confirm(employee3);
        list.add(confirm1);
        list.add(confirm2);
        list.add(confirm3);
        Mockito.when(confirmRepository.findAll()).thenReturn(list);

        //test
        List<Confirm> empList = confirmService.findAll();
        assertEquals(list.size(), empList.size());
    }


}
