package com.service;

import com.dto.AssignmentDTO;

public interface AssignmentService {

    AssignmentDTO createAssignment(AssignmentDTO assignmentDTO);
    boolean existsAssignmentByEmployee_CodeAndProject_Code(int codeEmployee,String codeProject);
}
