package com.example.Testsecure.Repositories;

import com.example.Testsecure.Model.Examiner;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ExaminerRepository extends CrudRepository<Examiner, Integer> {
    // For login
    Optional<Examiner> findByEmail(String email);

    // Get all examiners created by a specific admin
    List<Examiner> findByAdminId(Long adminId);

    // Check if examiner email already exists
    boolean existsByEmail(String email);

    Optional<Object> findById(Long examinerId);

}
