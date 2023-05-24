package com.up9e.exam.service;

import com.up9e.exam.entity.ExamQuestion;
import com.up9e.exam.global.BusinessException;
import com.up9e.exam.global.ExamUserDetails;
import com.up9e.exam.recode.QuestionAnswer;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface ExamQuestionService {

    Page<ExamQuestion> getExamQuestionPage(String infoId, int page, int size);

    List<String> verifyAnswer(String infoId, List<String> answerList, String email, Date timestamp, String verify) throws BusinessException;
}
