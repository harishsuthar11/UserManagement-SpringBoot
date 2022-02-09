package com.example.UserAPI.dto;

import lombok.Data;


@Data

public class JwtRequest {

    private String username;
    private String password;

    public JwtRequest(String username, String password){
        this.username = username;
        this.password = password;
    }
}
