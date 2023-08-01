package com.dto;

import com.entity.ERoleProject;
import com.entity.Employee;
import com.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AssignmentDTO {
    private Long id;
    private Employee employee;
    private Project project;
    private ERoleProject roleProject;
}
