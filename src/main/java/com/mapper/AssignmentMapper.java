package com.mapper;



import com.dto.AssignmentDTO;
import com.entity.Assignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AssignmentMapper {

    AssignmentMapper MAPPER= Mappers.getMapper(AssignmentMapper.class);

    @Mapping(source = "roleProject",target = "roleProject")
    @Mapping(source = "employee.userName",target = "nameEmployee")
    @Mapping(source = "project.nameProject",target = "nameProject")
    AssignmentDTO assignmentToAssignmentDto(Assignment assignment);
    @Mapping(source = "roleProject",target = "roleProject")
    Assignment assignmentDtoToAssignment(AssignmentDTO assignmentDTO);
}
