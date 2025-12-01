package com.example.Testsecure.Controller;
import com.example.Testsecure.Model.Admin;
import com.example.Testsecure.Service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/admin")
@CrossOrigin("*")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> req) {
        return registrationService.startRegistration(req);
    }

    @PostMapping("/verify-otp")
    public Admin verifyOtp(@RequestBody Map<String, String> req) {
        return registrationService.verifyOtp(req);
    }
}
