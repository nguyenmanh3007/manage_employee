package com.dto;

import com.entity.Confirm;
import com.entity.Roles;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private int employeeId;
    private int code;
    private String userName;
    private String password;
    private String created;
    private String email;
    private String phone;
    private String timeCheckin="08:00:00";
    private String timeCheckout="17:00:00";
    private boolean employeeStatus;
    private Set<String> listRole=new HashSet<>();
    private Set<Roles> listRoles = new HashSet<>();
    private Set<Confirm> confirms=new HashSet<>();
}
