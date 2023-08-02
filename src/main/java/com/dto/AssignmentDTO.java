package com.dto;

import com.entity.ERoleProject;
import com.entity.Employee;
import com.entity.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String nameEmployee;
    private String nameProject;
    @JsonIgnore
    private Employee employee;
    @JsonIgnore
    private Project project;
    private ERoleProject roleProject;
}
