package com.example.Testsecure.Model;



import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "question_papers")
public class QuestionPaper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDate examDate;   // e.g. 2025-12-10
    private LocalTime examTime;   // e.g. 10:00

    private String filePath;      // e.g. exam-pdfs/1733312341234_math.pdf

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "examiner_id")
    private Examiner examiner;
}
