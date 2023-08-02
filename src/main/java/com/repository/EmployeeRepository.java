package com.repository;

import com.dto.EmCoDTO;
import com.dto.EmployeeDTO;
import com.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByUserName(String un);
    Employee findByEmployeeId(int id);
    Employee findByCode(int code);
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

    @Query(value = "SELECT em FROM Employee em WHERE em.userName LIKE %?1% order by em.userName ASC")
    List<Employee> findByUserNameASC(String username);

    @Query(value = "SELECT new com.entity.Employee(em.code, em.userName, em.phone,cf.timeCheckIn) FROM Employee em INNER JOIN Confirm cf ON em.employeeId = cf.employee.employeeId WHERE cf.timeCheckIn BETWEEN :dateStart AND :dateEnd")
    List<Employee> listEmployeeIOwithTime(@Param("dateStart") String dateStart, @Param("dateEnd") String dateEnd);

    @Query(value = "SELECT new com.dto.EmCoDTO(em.code,em.userName,em.phone,em.timeCheckin) From Employee em where  em.userName like %?1%")
    Page<EmCoDTO> findEmCoDTo(String username, Pageable pageable);

    @Query(value = "SELECT em FROM Assignment ag INNER JOIN Employee em ON em.employeeId=ag.employee.employeeId" +
            " INNER JOIN Project pj ON ag.project.projectId = pj.projectId " +
            " WHERE pj.code = ?1")
    List<Employee> searchEmployeesByProject(String code);

}
