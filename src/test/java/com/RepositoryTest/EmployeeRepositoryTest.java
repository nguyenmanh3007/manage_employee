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
        Employee employee = new Employee(1236, "Guptar","123","nam@gmail.com");

        employeeRepository.save(employee);

        Employee employee2 = employeeRepository.findByUserName("Guptar");
        Assertions.assertThat(employee2.getUserName().equals("Guptar"));
        employeeRepository.delete(employee2);
        Assertions.assertThat(employeeRepository.findByUserName("Guptar")).isNull();
    }
    @Test
    public void testlistNotCheckIn() {
        String date="25/07/2023";

        List<Employee> list= employeeRepository.listNotCheckIn(date);

        Assertions.assertThat(list.isEmpty()==false);
    }
}
