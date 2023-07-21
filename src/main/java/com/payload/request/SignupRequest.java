package com.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    private String userName;
    private String password;
    private String email;
    private String phone;
    private Date created;
    private String timeCheckin="08:00:00";
    private String timeCheckout="17:00:00";
    private boolean userStatus;
    private Set<String> listRoles;
}
