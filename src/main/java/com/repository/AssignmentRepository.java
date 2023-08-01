package com.repository;

import com.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment,Integer> {
            boolean existsAssignmentByEmployee_CodeAndProject_Code(int codeEmployee,String codeProject);
}
