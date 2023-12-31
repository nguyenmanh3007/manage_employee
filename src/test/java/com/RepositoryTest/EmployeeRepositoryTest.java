package com.RepositoryTest;


import com.entity.Employee;
import com.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(value = true)
public class EmployeeRepositoryTest {


    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    @Test
    public void testSavefindDelete() {
        Employee employee = Employee.builder()
                .code(1239)
                .userName("Guptar")
                .password("123")
                .email("nam@gmail.com")
                .phone("094324324")
                .build();
        employeeRepository.save(employee);
        Employee employee2 = employeeRepository.findByUserName("Guptar");
        Assertions.assertThat(employee2.getUserName()).isEqualTo("Guptar");
        employeeRepository.delete(employee2);
        Assertions.assertThat(employeeRepository.findByUserName("Guptar")).isNull();
    }
    @Test
    public void testlistNotCheckIn() {
        String date="2023-07-25";

        List<Employee> list= employeeRepository.listNotCheckIn(date);

        Assertions.assertThat(list).isNotEmpty();
    }
}
