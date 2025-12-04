package com.example.Testsecure.Controller;



import com.example.Testsecure.Dto.ResponseMsg;
import com.example.Testsecure.Model.QuestionPaper;
import com.example.Testsecure.Service.QuestionPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.format.annotation.DateTimeFormat;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping
public class QuestionPaperController {

    @Autowired
    private QuestionPaperService paperService;

    // ---------- ADMIN: Upload & Assign ----------
    @PostMapping(
            value = "/admin/upload-paper",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseMsg uploadPaper(
            @RequestHeader("Authorization") String auth,
            @RequestParam("title") String title,
            @RequestParam("examDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate examDate,
            @RequestParam("examTime")
            @DateTimeFormat(pattern = "HH:mm") LocalTime examTime,
            @RequestParam("examinerId") Long examinerId,
            @RequestParam("file") MultipartFile file
    ) {

        QuestionPaper saved = paperService.uploadAndAssign(
                auth, title, examDate, examTime, examinerId, file
        );
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setMsg("Paper uploaded & assigned with id: " + saved.getId());

        return responseMsg;
    }

    // ---------- EXAMINER: Download (time-locked) ----------
    @GetMapping("/examiner/download-paper/{paperId}")
    public ResponseEntity<?> downloadPaper(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long paperId
    ) {
        Path filePath = paperService.getFileForDownload(auth, paperId);

        FileSystemResource resource = new FileSystemResource(filePath.toFile());

        String fileName = filePath.getFileName().toString();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF) // assuming pdf
                .body(resource);
    }
    @GetMapping("examiner/papers")
    public List<QuestionPaper> getAllQuestionPaper( @RequestHeader("Authorization") String auth){
        return paperService.getQuestionPaperByExaminerId(auth);

    }
}
