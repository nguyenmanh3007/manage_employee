package com.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmCoDTO {

    private int code;
    private String username;
    private String phone;
    private String statusCheckIn;

    public EmCoDTO(int code, String username, String phone) {
        this.code = code;
        this.username = username;
        this.phone = phone;
    }
}
