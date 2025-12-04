package com.example.Testsecure.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String universityName;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Examiner> examiners;

    public Admin() {}
}
