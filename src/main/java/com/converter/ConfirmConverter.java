package com.converter;

import com.dto.EmployeeWithConfirmDTO;
import com.entity.Confirm;
import org.springframework.stereotype.Component;

@Component
public class ConfirmConverter {
    public EmployeeWithConfirmDTO toDTO(Confirm confirm){
        EmployeeWithConfirmDTO employeeWithConfirmDTO= new EmployeeWithConfirmDTO();
        employeeWithConfirmDTO.setCode(confirm.getEmployee().getCode());
        employeeWithConfirmDTO.setUserName(confirm.getEmployee().getUserName());
        employeeWithConfirmDTO.setTimeCheckIn(confirm.getTimeCheckIn());
        employeeWithConfirmDTO.setTimCheckOut(confirm.getTimeCheckOut());
        employeeWithConfirmDTO.setCheckInLate(confirm.getCheckInLate());
        employeeWithConfirmDTO.setCheckOutEarly(confirm.getCheckOutEarly());
        employeeWithConfirmDTO.setStatusCheckIn(confirm.getStatusCheckIn());
        employeeWithConfirmDTO.setStatusCheckOut(confirm.getStatusCheckOut());

        return employeeWithConfirmDTO;
    }
}
