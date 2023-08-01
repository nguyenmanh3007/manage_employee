package com.service.impl;


import com.dto.AssignmentDTO;
import com.mapper.AssignmentMapper;
import com.repository.AssignmentRepository;
import com.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Override
    public AssignmentDTO createAssignment(AssignmentDTO assignmentDTO) {
        return AssignmentMapper.MAPPER.assignmentToAssignmentDto(assignmentRepository.save(AssignmentMapper.MAPPER.assignmentDtoToAssignment(assignmentDTO)));
    }

    @Override
    public boolean existsAssignmentByEmployee_CodeAndProject_Code(int codeEmployee, String codeProject) {
        return assignmentRepository.existsAssignmentByEmployee_CodeAndProject_Code(codeEmployee,codeProject);
    }
}
