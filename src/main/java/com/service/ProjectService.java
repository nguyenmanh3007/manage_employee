package com.service;

import com.dto.EmployeeDTO;
import com.dto.ProjectDTO;
import com.entity.Project;

public interface ProjectService {

    ProjectDTO createProject(ProjectDTO projectDTO);
    ProjectDTO updateProject(ProjectDTO projectDTO);

    void deleteProject(int idProject);
    Project findByCode(String code);

    ProjectDTO findByIdProject(int id);
    boolean existsByCode(String code);

}
