package com.service.impl;


import com.dto.ProjectDTO;
import com.entity.Project;
import com.mapper.ProjectMapper;
import com.repository.ProjectRepository;
import com.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Date now= new Date();
        SimpleDateFormat format =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        projectDTO.setCreateDay(format.format(now));
        Project project= ProjectMapper.MAPPER.projectDtoToProject(projectDTO);
        return ProjectMapper.MAPPER.projectToProjectDto(projectRepository.save(project));
    }

    @Override
    public ProjectDTO updateProject(ProjectDTO projectDTO) {
        ProjectDTO oldProject= findByIdProject(projectDTO.getProjectId());
        projectDTO= projectDTO.builder()
                .projectId(projectDTO.getProjectId())
                .code(projectDTO.getCode())
                .nameProject(projectDTO.getNameProject())
                .createDay(oldProject.getCreateDay())
                .assignments(oldProject.getAssignments())
                .comments(oldProject.getComments())
                .build();
        Project project= ProjectMapper.MAPPER.projectDtoToProject(projectDTO);
        return ProjectMapper.MAPPER.projectToProjectDto(projectRepository.save(project));
    }

    @Override
    public void deleteProject(int idProject) {
        projectRepository.deleteById(idProject);
    }

    @Override
    public Project findByCode(String code) {
        return projectRepository.findByCode(code);
    }

    @Override
    public ProjectDTO findByIdProject(int id) {
        return ProjectMapper.MAPPER.projectToProjectDto(projectRepository.findByProjectId(id));
    }

    @Override
    public boolean existsByCode(String code) {
        return projectRepository.existsByCode(code);
    }

    @Override
    public boolean existsByNameProject(String nameProject) {
        return projectRepository.existsByNameProject(nameProject);
    }
}
