package com.dto;

import com.entity.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmDTO {
    private int code;
    private String userName;
    private String timeCheckIn;
    private String timeCheckOut;
    private int checkInLate;
    private int checkOutEarly;
    private String statusCheckIn;
    private String statusCheckOut;
    @JsonIgnore
    private Employee employee;
}
