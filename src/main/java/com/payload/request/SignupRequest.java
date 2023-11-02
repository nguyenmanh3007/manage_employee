package com.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @NotNull(message = "username is not null")
    private String userName;
    @NotNull(message = "password is not null")
    private String password;

    @Email(message = "email is not correct")
    private String email;

    private String phone;

    private Date created;

    private String timeCheckin="08:00:00";

    private String timeCheckout="17:00:00";

    private boolean userStatus;

    private Set<String> listRoles;
}
