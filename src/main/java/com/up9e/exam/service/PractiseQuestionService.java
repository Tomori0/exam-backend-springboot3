package com.up9e.exam.service;

import com.up9e.exam.entity.ExamQuestion;
import com.up9e.exam.global.BusinessException;
import com.up9e.exam.global.ExamUserDetails;
import com.up9e.exam.recode.QuestionAnswer;
import org.springframework.data.domain.Page;

public interface PractiseQuestionService {

    Page<ExamQuestion> getExamQuestionPage(String infoId, int page, int size);

    String generateToken(ExamUserDetails userDetails, String infoId) throws BusinessException;

    void verifyToken(ExamUserDetails userDetails, QuestionAnswer questionAnswer) throws BusinessException;

    String getAnswer(String questionId);
}
