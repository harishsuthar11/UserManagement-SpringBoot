package com.example.UserAPI.jwt.entity;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
