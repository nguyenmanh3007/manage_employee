package com.mapper;

import com.dto.EmployeeDTO;
import com.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {
    EmployeeMapper MAPPER= Mappers.getMapper(EmployeeMapper.class);


    @Mapping(source = "userName",target = "userName")
    EmployeeDTO employeeToEmployeeDto(Employee employee);

    Employee employeeDtoToEmployee(EmployeeDTO employeeDTO);
}
