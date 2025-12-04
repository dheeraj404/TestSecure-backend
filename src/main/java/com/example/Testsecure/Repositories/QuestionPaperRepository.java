package com.example.Testsecure.Repositories;



import com.example.Testsecure.Model.QuestionPaper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QuestionPaperRepository extends JpaRepository<QuestionPaper, Long> {

    // For checking if examiner already has a paper on that date
    Optional<QuestionPaper> findByExaminerIdAndExamDate(Long examinerId, LocalDate examDate);

    // To show papers to examiner
    List<QuestionPaper> findByExaminerId(Long examinerId);

    // To show all papers uploaded by an admin (if needed)
    List<QuestionPaper> findByAdminId(Long adminId);
}
