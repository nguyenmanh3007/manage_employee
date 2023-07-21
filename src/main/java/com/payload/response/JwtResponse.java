package com.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private int code;
    private String type = "Bearer";
    private String userName;
    private String email;
    private String phone;
    private String timeCheckin;
    private String timeCheckout;
    private List<String> listRoles;
    public JwtResponse(String token, String type, String userName, String email, String phone, List<String> listRoles) {
        super();
        this.token = token;
        this.type = type;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.listRoles = listRoles;
    }

    public JwtResponse(String token, int code, String userName, String email, String phone, String timeCheckin, String timeCheckout, List<String> listRoles) {
        this.token = token;
        this.code = code;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.timeCheckin = timeCheckin;
        this.timeCheckout = timeCheckout;
        this.listRoles = listRoles;
    }
}
