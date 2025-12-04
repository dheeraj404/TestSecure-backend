package com.example.Testsecure.Controller;

import com.example.Testsecure.Dto.CreateExaminerRequest;
import com.example.Testsecure.Model.Examiner;
import com.example.Testsecure.Repositories.ExaminerRepository;
import com.example.Testsecure.Service.ExaminerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class ExaminerController {

@Autowired
   ExaminerService examinerService;
@Autowired
ExaminerRepository examinerRepository;
    @PostMapping("/examiner/create")
    Examiner createExaminer(@RequestHeader("Authorization") String auth,
                            @RequestBody CreateExaminerRequest req){
        return  examinerService.CreateExaminer(auth,req);

    }
    @GetMapping("/examiner")
    List<Examiner> getExaminerList(@RequestHeader("Authorization") String auth){
        return examinerService.getExaminerList(auth);
    }


}
