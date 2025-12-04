package com.example.Testsecure.Dto;

import lombok.Data;

@Data
public class CreateExaminerRequest {
    private String email;
    private String password;
    private String name;
    private String subject;
}
