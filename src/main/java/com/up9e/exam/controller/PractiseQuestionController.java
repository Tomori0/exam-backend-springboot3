package com.up9e.exam.controller;

import com.up9e.exam.entity.ExamQuestion;
import com.up9e.exam.global.BusinessException;
import com.up9e.exam.global.ExamUserDetails;
import com.up9e.exam.global.ResponseApi;
import com.up9e.exam.recode.QuestionAnswer;
import com.up9e.exam.service.PractiseQuestionService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exam/practise")
public class PractiseQuestionController {

    private final PractiseQuestionService practiseQuestionService;

    public PractiseQuestionController(PractiseQuestionService practiseQuestionService) {
        this.practiseQuestionService = practiseQuestionService;
    }

    @GetMapping("/get")
    public ResponseApi<Page<ExamQuestion>> get(@AuthenticationPrincipal ExamUserDetails userDetails,
                                               @RequestParam String infoId,
                                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                                               @RequestParam(value = "size", defaultValue = "1") Integer size) throws BusinessException {
        Page<ExamQuestion> examQuestionPage = practiseQuestionService.getExamQuestionPage(infoId, page, size);
        String token = practiseQuestionService.generateToken(userDetails, infoId);
        return new ResponseApi<>(200, examQuestionPage, token);
    }

    @PostMapping("/answer")
    public ResponseApi<String> answer(@AuthenticationPrincipal ExamUserDetails userDetails,
                                      @RequestBody QuestionAnswer questionAnswer) throws BusinessException {
        practiseQuestionService.verifyToken(userDetails, questionAnswer);
        return new ResponseApi<>(practiseQuestionService.getAnswer(questionAnswer.questionId()));
    }
}
