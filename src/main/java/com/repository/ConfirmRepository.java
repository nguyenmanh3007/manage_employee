package com.repository;

import com.dto.EmWithDto;
import com.entity.Confirm;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConfirmRepository extends JpaRepository<Confirm,Integer> {
    @Modifying
    @Query(value = "delete from employee_role where EmployeeId=?1",nativeQuery = true)
    void deleteByEmployeeId(int id);
    @Query(value = "SELECT cf FROM Confirm cf WHERE cf.timeCheckIn LIKE ?1% AND cf.employee.employeeId=?2")
    Confirm checkEmployeeCheckedIn(String timeCheck, int id);
    @Query(value = "SELECT cf FROM Confirm cf INNER JOIN Employee em ON cf.employee.employeeId = em.employeeId" +
            " WHERE cf.timeCheckIn BETWEEN ?1 AND ?2")
    Page<Confirm> listEmployeeCheckIO(String dateStart,String dateEnd, Pageable pageable);

    @Query(value = "SELECT cf FROM Confirm cf where cf.timeCheckIn LIKE ?1% AND (cf.statusCheckIn ='dLate' OR cf.statusCheckIn is null)")
    List<Confirm> listEmployeeCheckInError(String time);

    //employee
    @Query(value = "SELECT cf FROM Confirm cf INNER JOIN Employee em ON cf.employee.employeeId=em.employeeId " +
            "WHERE SUBSTRING(cf.timeCheckIn, 1, 10) BETWEEN ?1 AND ?2 AND em.userName = ?3")
    List<Confirm> listCheckIOForEmployee(String dateStart, String dateEnd,String username);
    @Query(value = "SELECT cf FROM Confirm cf,Employee em  where cf.timeCheckIn LIKE %?1% AND (cf.statusCheckIn ='dLate' OR cf.statusCheckIn is null) AND cf.employee.employeeId=em.employeeId AND em.userName=?2")
    List<Confirm> listCheckIOErrorForEmployee(String time,String username);

    //close Projection
//    @Query(value = "SELECT employee.Code as code,employee.Username as userName, confirm.timeCheckIn as timeCheckIn, confirm.timeCheckOut as timeCheckOut,confirm.checkInLate as checkInLate,confirm.checkOutEarly as checkOutEarly,confirm.statusCheckIn as statusCheckIn, confirm.statusCheckOut as statusCheckOut FROM confirm,employee where confirm.employeeId=employee.EmployeeId AND (confirm.timeCheckIn BETWEEN :dateStart AND :dateEnd)",nativeQuery = true)
//    List<EmWithDto> listTest(@Param("dateStart") String dateStart, @Param("dateEnd") String dateEnd);
    //open Projection
    @Query(value = "SELECT cf  FROM Confirm cf where cf.timeCheckIn BETWEEN :dateStart AND :dateEnd")
    List<EmWithDto> listTest(@Param("dateStart") String dateStart, @Param("dateEnd") String dateEnd);

    @Query(value = "SELECT cf from Confirm cf where cf.id=?1")
    Confirm findById(int id);


}
