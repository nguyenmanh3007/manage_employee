package com.service;

import com.dto.EmWithDto;
import com.entity.Confirm;
import com.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConfirmService {
    Confirm saveOrUpdate(Confirm confirm);
    List<Confirm> findAll();
    boolean checkEmployeeCI(int EmployeeId);
    Confirm checkEmployeeCheckedIn(String timeCheck, int id);

    List<Confirm> listEmployeeCheckIO(String dStart,String dEnd);
    List<Confirm> listEmployeeCheckInError(String time);
    List<Confirm> listCheckIOForEmployee(String dateStart,String dateEnd,String username);
    List<Confirm> listCheckIOErrorForEmployee(String time,String username);
    List<EmWithDto> listTest(String dateStart,String dateEnd);
}
