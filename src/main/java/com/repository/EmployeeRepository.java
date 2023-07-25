package com.repository;

import com.dto.EmCoDTO;
import com.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByUserName(String un);
    boolean existsByUserName(String un);
    boolean existsByEmail(String email);
    void deleteByEmployeeId(int id);
    @Modifying
    @Query(value = "delete from employee_role where EmployeeId=?1",nativeQuery = true)
    void deleteRoleEmployee(int id);
    @Query(value = "SELECT * FROM employee AS cf WHERE cf.EmployeeId " +
            "IN (SELECT employeeId FROM confirm WHERE timeCheckIn LIKE %?1% AND timeCheckOut is null  GROUP BY employeeId HAVING COUNT(*) = 1)", nativeQuery = true)
    List<Employee> listNotCheckOut(String date);
    @Query(value = "SELECT * FROM employee AS cf WHERE cf.EmployeeId " +
            "NOT IN (SELECT employeeId FROM confirm WHERE timeCheckIn LIKE %?1% GROUP BY employeeId HAVING COUNT(*)>0)", nativeQuery = true)
    List<Employee> listNotCheckIn(String date);

    @Query(value = "SELECT * FROM employee AS cf WHERE cf.Username LIKE %?1% order by cf.Username ASC",nativeQuery = true)
    List<Employee> findByUserNameASC(String username);

//    @Query(value = "SELECT new com.entity.Employee(em.Code,em.Username,em.Phone) FROM employee em, confirm cf WHERE em.EmployeeId=cf.employeeId",nativeQuery = true)
//    List<Employee> listEmployeeIOwithTime(String dateStart, String dateEnd);

    @Query(value = "SELECT new com.entity.Employee(em.Code, em.Username, em.Phone) FROM employee em INNER JOIN confirm cf ON em.employee_id = cf.employee_id WHERE cf.timeCheckIn BETWEEN :dateStart AND :dateEnd", nativeQuery = true)
    List<Employee> listEmployeeIOwithTime(@Param("dateStart") String dateStart, @Param("dateEnd") String dateEnd);

    @Query(value = "SELECT new com.dto.EmCoDTO(em.code,em.userName,em.phone) From Employee em where  em.userName like %?1%")
    Page<EmCoDTO> findEmCoDTo(String username, Pageable pageable);

}
