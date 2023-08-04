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
//        Employee employee1 = new Employee(1236, "Guptar1","123","nam1@gmail.com");
//        Employee employee2 = new Employee(1237, "Guptar2","123","nam2@gmail.com");
//        Employee employee3 = new Employee(1238, "Guptar3","123","nam3@gmail.com");
//        Confirm confirm1 = new Confirm(employee1);
//        Confirm confirm2 = new Confirm(employee2);
//        Confirm confirm3 = new Confirm(employee3);
//        list.add(confirm1);
//        list.add(confirm2);
//        list.add(confirm3);
        Mockito.when(confirmRepository.findAll()).thenReturn(list);

        //test
        List<Confirm> empList = confirmService.findAll();
        assertEquals(list.size(), empList.size());
    }


}
