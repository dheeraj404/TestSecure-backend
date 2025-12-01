package com.example.Testsecure.Dto;


import lombok.Data;

@Data
public class AdminRegisterRequest {
    private String universityName;
    private String email;
    private String password;
}
