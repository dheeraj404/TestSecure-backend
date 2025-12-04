package com.example.Testsecure.Service;
import com.example.Testsecure.Model.Admin;
import com.example.Testsecure.Dto.AdminRegisterRequest;
import com.example.Testsecure.Dto.VerifyOtpRequest;
import com.example.Testsecure.Model.TempRegistration;
import com.example.Testsecure.Repositories.AdminRepository;
import com.example.Testsecure.Repositories.TempRegistrationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class RegistrationService {

    @Autowired
    private TempRegistrationRepository tempRepo;

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // STEP-1: Save data + send OTP
    public ResponseEntity<Map<String, String>> startRegistration(Map<String, String> req) {

        String email = req.get("email");

        String universityName = req.get("universityName");
        String password = req.get("password");
        if (adminRepo.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        String otp = String.format("%06d", new Random().nextInt(999999));

        TempRegistration temp = tempRepo.findByEmail(email)
                .orElse(new TempRegistration());

        temp.setEmail(email);
        temp.setUniversityName(universityName);
        temp.setPassword(password);
        temp.setOtp(otp);
        temp.setExpiry(LocalDateTime.now().plusMinutes(10));

        tempRepo.save(temp);

        sendEmail(email, otp);

        return ResponseEntity.ok(Map.of("message", "OTP sent to email"));
    }

    private void sendEmail(String to, String otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Your OTP for Registration");
        msg.setText("Your OTP is: " + otp + " valid for 10 minutes.");
        mailSender.send(msg);
    }

    // STEP-2: Verify OTP + create Admin
    @Transactional
    public Admin verifyOtp(Map<String, String> req) {

        String email = req.get("email");
        String otp = req.get("otp");

        TempRegistration temp = tempRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No pending registration found"));

        if (temp.getExpiry().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP expired");

        if (!temp.getOtp().equals(otp))
            throw new RuntimeException("Invalid OTP");

        Admin admin = new Admin();
        admin.setEmail(temp.getEmail());
        admin.setUniversityName(temp.getUniversityName());
        admin.setPassword(passwordEncoder.encode(temp.getPassword()));

        adminRepo.save(admin);

        tempRepo.delete(temp);

        return admin;
    }
}
