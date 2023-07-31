package com.service;

import com.dto.EmWithDto;
import com.entity.Confirm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ConfirmService {
    Confirm saveOrUpdate(Confirm confirm);
    List<Confirm> findAll();
    boolean checkEmployeeCI(int EmployeeId);
    Confirm checkEmployeeCheckedIn(String timeCheck, int id);

    Page<Confirm> listEmployeeCheckIO(String dStart, String dEnd, Pageable pageable);
    List<Confirm> listEmployeeCheckInError(String time);
    List<Confirm> listCheckIOForEmployee(String dateStart,String dateEnd,String username);
    List<Confirm> listCheckIOErrorForEmployee(String time,String username);
    List<EmWithDto> listTest(String dateStart,String dateEnd);


    Confirm findById(int id);
    void updateca();
}
