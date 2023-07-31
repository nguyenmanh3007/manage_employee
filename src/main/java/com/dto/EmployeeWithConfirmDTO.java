package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeWithConfirmDTO {
    private int code;
    private String userName;
    private String timeCheckIn;
    private String timeCheckOut;
    private int checkInLate;
    private int checkOutEarly;
    private String statusCheckIn;
    private String statusCheckOut;
}
