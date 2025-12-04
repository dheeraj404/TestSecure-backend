package com.example.Testsecure.Service;
import com.example.Testsecure.Model.Admin;
import com.example.Testsecure.Model.Examiner;
import com.example.Testsecure.Model.QuestionPaper;
import com.example.Testsecure.Repositories.AdminRepository;
import com.example.Testsecure.Repositories.ExaminerRepository;
import com.example.Testsecure.Repositories.QuestionPaperRepository;
import com.example.Testsecure.Utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionPaperService {

    private static final String UPLOAD_DIR = "exam-pdfs";

    @Autowired
    private QuestionPaperRepository paperRepo;

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private ExaminerRepository examinerRepo;

    @Autowired
    private JwtUtil jwtUtil;

    // ---------- UPLOAD + ASSIGN ----------
    public QuestionPaper uploadAndAssign(
            String authHeader,
            String title,
            LocalDate examDate,
            LocalTime examTime,
            Long examinerId,
            MultipartFile file
    ) {

        String token = authHeader.substring(7);
        String role = jwtUtil.getRole(token);
        Long adminId = jwtUtil.getId(token);

        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Only admin can upload papers");
        }

        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Object examiner = examinerRepo.findById(examinerId)
                .orElseThrow(() -> new RuntimeException("Examiner not found"));

        // RULE: Examiner can't have two papers on same exam date
        Optional<QuestionPaper> existing =
                paperRepo.findByExaminerIdAndExamDate(examinerId, examDate);

        if (existing.isPresent()) {
            throw new RuntimeException("Examiner already has a paper on this date");
        }

        // Ensure folder exists
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }

        // Generate unique filename
        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }
        String uniqueName = UUID.randomUUID() + ext;
        Path targetPath = Paths.get(UPLOAD_DIR).resolve(uniqueName);

        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }

        QuestionPaper paper = new QuestionPaper();
        paper.setTitle(title);
        paper.setExamDate(examDate);
        paper.setExamTime(examTime);
        paper.setFilePath(targetPath.toString());
        paper.setAdmin(admin);
        paper.setExaminer((Examiner) examiner);

        return paperRepo.save(paper);
    }

    // ---------- DOWNLOAD (with time rule) ----------
    public Path getFileForDownload(String authHeader, Long paperId) {

        String token = authHeader.substring(7);
        String role = jwtUtil.getRole(token);
        Long userId = jwtUtil.getId(token);

        QuestionPaper paper = paperRepo.findById(paperId)
                .orElseThrow(() -> new RuntimeException("Paper not found"));

        // Only assigned examiner can download
        if (!"EXAMINER".equals(role) || !paper.getExaminer().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to download this paper");
        }

        // Time rule: allow only within 30 minutes before exam time
        LocalDateTime examDateTime = LocalDateTime.of(paper.getExamDate(), paper.getExamTime());
        LocalDateTime allowedFrom = examDateTime.minusMinutes(30);

        if (LocalDateTime.now().isBefore(allowedFrom)) {
            throw new RuntimeException("Download allowed only 30 minutes before exam time");
        }

        Path path = Paths.get(paper.getFilePath());
        if (!Files.exists(path)) {
            throw new RuntimeException("File not found on server");
        }

        return path;
    }
    public List<QuestionPaper> getQuestionPaperByExaminerId( @RequestHeader("Authorization") String auth) {
        String token = auth.substring(7);
        String role = jwtUtil.getRole(token);
        Long examinerId = jwtUtil.getId(token);
        return paperRepo.findByExaminerId(examinerId);
    }

}
