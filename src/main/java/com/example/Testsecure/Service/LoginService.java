package com.example.Testsecure.Service;

import com.example.Testsecure.Dto.LoginRequest;
import com.example.Testsecure.Dto.LoginResponse;
import com.example.Testsecure.Model.Admin;
import com.example.Testsecure.Model.Examiner;
import com.example.Testsecure.Repositories.AdminRepository;
import com.example.Testsecure.Repositories.ExaminerRepository;
import com.example.Testsecure.Utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private ExaminerRepository examinerRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse loginAdmin(LoginRequest req) {
        Admin admin = adminRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!encoder.matches(req.getPassword(), admin.getPassword()))
            throw new RuntimeException("Invalid password");

        String token = jwtUtil.generateToken(admin.getId(), "ADMIN");

        return new LoginResponse(token, admin.getId(), "ADMIN","");
    }

    public LoginResponse loginExaminer(LoginRequest req) {
        Examiner ex = examinerRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Examiner not found"));

        if (!encoder.matches(req.getPassword(), ex.getPassword()))
            throw new RuntimeException("Invalid password");

        String token = jwtUtil.generateToken(ex.getId(), "EXAMINER");

        return new LoginResponse(token, ex.getId(), "EXAMINER",ex.getName());
    }
}
