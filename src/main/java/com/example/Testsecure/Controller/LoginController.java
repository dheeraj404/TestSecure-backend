package com.example.Testsecure.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Testsecure.Dto.LoginRequest;
import com.example.Testsecure.Dto.LoginResponse;
import com.example.Testsecure.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping("/admin/login")
    public ResponseEntity<LoginResponse> loginAdmin(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(loginService.loginAdmin(req));
    }
    @PostMapping("/examiner/login")
    public ResponseEntity<LoginResponse> loginExaminer(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(loginService.loginExaminer(req));
    }
}
