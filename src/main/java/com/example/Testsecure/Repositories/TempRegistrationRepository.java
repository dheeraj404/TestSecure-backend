package com.example.Testsecure.Repositories;

import com.example.Testsecure.Model.TempRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempRegistrationRepository extends JpaRepository<TempRegistration, Long> {
    Optional<TempRegistration> findByEmail(String email);
}
