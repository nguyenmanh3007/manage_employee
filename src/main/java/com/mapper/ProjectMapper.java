package com.mapper;

import com.dto.ProjectDTO;
import com.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectMapper {
    ProjectMapper MAPPER= Mappers.getMapper(ProjectMapper.class);


    @Mapping(source="code",target="code")
    ProjectDTO projectToProjectDto(Project project);

    @Mapping(source="code",target="code")
    @Mapping(source="createDay",target="createDay")
    Project projectDtoToProject(ProjectDTO projectDTO);
}
