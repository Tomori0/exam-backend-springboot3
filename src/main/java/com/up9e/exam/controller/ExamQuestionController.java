package com.up9e.exam.controller;

import com.up9e.exam.entity.ExamQuestion;
import com.up9e.exam.global.BusinessException;
import com.up9e.exam.global.ExamUserDetails;
import com.up9e.exam.global.ResponseApi;
import com.up9e.exam.recode.ExamSubmit;
import com.up9e.exam.service.ExamQuestionService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam/question")
public class ExamQuestionController {

    private final ExamQuestionService examQuestionService;

    public ExamQuestionController(ExamQuestionService examQuestionService) {
        this.examQuestionService = examQuestionService;
    }

    @GetMapping("/get")
    public ResponseApi<Page<ExamQuestion>> get(@RequestParam String infoId,
                                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                                               @RequestParam(value = "size", defaultValue = "1") Integer size) throws BusinessException {
        Page<ExamQuestion> examQuestionPage = examQuestionService.getExamQuestionPage(infoId, page, size);
        return new ResponseApi<>(200, examQuestionPage);
    }

    @PostMapping("/submit")
    public ResponseApi<List<String>> submit(@AuthenticationPrincipal ExamUserDetails userDetails,
                                            @RequestBody ExamSubmit examSubmit) throws BusinessException {
        String email = userDetails.getEmail();
        return new ResponseApi<>(examQuestionService.verifyAnswer(examSubmit.infoId(), examSubmit.answer(), email, examSubmit.timestamp(), examSubmit.verify()));
    }
}
