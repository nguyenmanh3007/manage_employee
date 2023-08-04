package com.mapper;



import com.dto.ConfirmDTO;
import com.entity.Confirm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ConfirmMapper {

    ConfirmMapper MAPPER= Mappers.getMapper(ConfirmMapper.class);


    @Mapping(source = "confirm.employee.code",target = "code")
    @Mapping(source = "confirm.employee.userName",target = "userName")
    @Mapping(source = "confirm.timeCheckOut", target = "timeCheckOut")
    ConfirmDTO confirmToConfirmDTO(Confirm confirm);


}
