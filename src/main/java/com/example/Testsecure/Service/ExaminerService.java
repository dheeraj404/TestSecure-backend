package com.example.Testsecure.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.Testsecure.Dto.CreateExaminerRequest;
import com.example.Testsecure.Model.Admin;
import com.example.Testsecure.Model.Examiner;
import com.example.Testsecure.Repositories.AdminRepository;
import com.example.Testsecure.Repositories.ExaminerRepository;
import com.example.Testsecure.Utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Service
public class ExaminerService {
    @Autowired
    private ExaminerRepository  examinerRepo;
    @Autowired
    private AdminRepository adminRepo;
    @Autowired
   private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Examiner CreateExaminer( @RequestHeader("Authorization") String auth,
                                    @RequestBody CreateExaminerRequest req) {
        String token = auth.substring(7);
        String role = jwtUtil.getRole(token);

        if (!role.equals("ADMIN"))
            throw new RuntimeException("Only admin can create examiners");
      if(examinerRepo.existsByEmail(req.getEmail())){
          throw new RuntimeException("User already exists");
      }
        Long adminId = jwtUtil.getId(token);
        Admin admin = adminRepo.findById(adminId).get();

        Examiner ex = new Examiner();
        ex.setName(req.getName());
        ex.setEmail(req.getEmail());
        ex.setPassword(passwordEncoder.encode(req.getPassword()));
        ex.setSubject(req.getSubject());
        ex.setAdmin(admin);
        examinerRepo.save(ex);

        return ex;
    }
    public List<Examiner> getExaminerList(@RequestHeader("Authorization") String auth){
        String token = auth.substring(7);
        String role = jwtUtil.getRole(token);
        if (!role.equals("ADMIN"))
            throw new RuntimeException("Only admin can acess examiners");

        Long adminId = jwtUtil.getId(token);
        return examinerRepo.findByAdminId(adminId);
    }
}
