package com.repository;

import com.dto.EmWithDto;
import com.entity.Confirm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConfirmRepository extends JpaRepository<Confirm,Integer> {
    @Modifying
    @Query(value = "delete from employee_role where EmployeeId=?1",nativeQuery = true)
    void deleteByEmployeeId(int id);
    @Query(value = "SELECT * FROM confirm WHERE timeCheckIn LIKE ?1% AND employeeId=?2",nativeQuery = true)
    Confirm checkEmployeeCheckedIn(String timeCheck, int id);
    @Query(value = "SELECT * FROM confirm where timeCheckIn BETWEEN :dateStart AND :dateEnd",nativeQuery = true)
    List<Confirm> listEmployeeCheckIO(@Param("dateStart") String dateStart, @Param("dateEnd") String dateEnd);
    @Query(value = "SELECT * FROM confirm where timeCheckIn LIKE %?1% AND (statusCheckIn ='dLate' OR statusCheckIn is null)",nativeQuery = true)
    List<Confirm> listEmployeeCheckInError(String time);

    //employee
    @Query(value = "SELECT * FROM confirm as cf, employee where (cf.timeCheckIn BETWEEN :dateStart AND :dateEnd) AND cf.employeeId=employee.EmployeeId AND employee.Username=:username ",nativeQuery = true)
    List<Confirm> listCheckIOForEmployee(@Param("dateStart") String dateStart, @Param("dateEnd") String dateEnd,@Param("username") String username);
    @Query(value = "SELECT * FROM confirm as cf,employee where cf.timeCheckIn LIKE %?1% AND (statusCheckIn ='dLate' OR statusCheckIn is null) AND cf.employeeId=employee.EmployeeId AND employee.Username=?2",nativeQuery = true)
    List<Confirm> listCheckIOErrorForEmployee(String time,String username);

    @Query(value = "SELECT employee.Code as code,employee.Username as userName, confirm.timeCheckIn as timeCheckIn, confirm.timeCheckOut as timeCheckOut,confirm.checkInLate as checkInLate,confirm.checkOutEarly as checkOutEarly,confirm.statusCheckIn as statusCheckIn, confirm.statusCheckOut as statusCheckOut FROM confirm,employee where confirm.employeeId=employee.EmployeeId AND (confirm.timeCheckIn BETWEEN :dateStart AND :dateEnd)",nativeQuery = true)
    List<EmWithDto> listTest(@Param("dateStart") String dateStart, @Param("dateEnd") String dateEnd);




}
