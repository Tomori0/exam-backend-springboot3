package com.up9e.exam.service.impl;

import com.up9e.exam.constant.Constant;
import com.up9e.exam.constant.ErrorEnum;
import com.up9e.exam.dao.ExamQuestionDao;
import com.up9e.exam.entity.ExamQuestion;
import com.up9e.exam.global.BusinessException;
import com.up9e.exam.global.ExamUserDetails;
import com.up9e.exam.recode.QuestionAnswer;
import com.up9e.exam.service.PractiseQuestionService;
import com.up9e.exam.util.EncryptUtils;
import com.up9e.exam.util.RedisUtils;
import io.micrometer.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class PractiseQuestionServiceImpl implements PractiseQuestionService {

    private final ExamQuestionDao examQuestionDao;

    public PractiseQuestionServiceImpl(ExamQuestionDao examQuestionDao) {
        this.examQuestionDao = examQuestionDao;
    }

    @Override
    public Page<ExamQuestion> getExamQuestionPage(String infoId, int page, int size) {
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setInfoId(infoId);
        Example<ExamQuestion> examQuestionExample =
                Example.of(examQuestion, ExampleMatcher.matchingAll().withMatcher("info_id", ExampleMatcher.GenericPropertyMatchers.exact()));
        Page<ExamQuestion> examQuestionPage = examQuestionDao.findAll(examQuestionExample, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "questionId")));
        examQuestionPage.get().forEach(ele -> {
            ele.setQuestionAnswer(null);
        });
        return examQuestionPage;
    }

    @Override
    public String generateToken(ExamUserDetails userDetails, String infoId) throws BusinessException {
        String tokenKey = userDetails.getEmail() + Constant.STR_SALT + infoId;
        String token = RedisUtils.get(tokenKey);
        if (StringUtils.isEmpty(token)) {
            try {
                token = EncryptUtils.getSecretKey(256);
            } catch (NoSuchAlgorithmException e) {
                throw new BusinessException(ErrorEnum.ERROR_GENERATE_KEY);
            }
            RedisUtils.set(tokenKey, token);
        }
        RedisUtils.expire(tokenKey, 2, TimeUnit.HOURS);
        return token;
    }

    @Override
    public void verifyToken(ExamUserDetails userDetails, QuestionAnswer questionAnswer) throws BusinessException {
        String tokenKey = userDetails.getEmail() + Constant.STR_SALT + questionAnswer.infoId();
        String token = RedisUtils.get(tokenKey);
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(ErrorEnum.ERROR_ANSWER_TOKEN);
        }
        String message = questionAnswer.infoId() + questionAnswer.questionId() + questionAnswer.timestamp().getTime() + token;
        try {
            String encoded = EncryptUtils.encode(token, message);
            if (!encoded.equals(questionAnswer.verify())) {
                log.warn(String.format("验签失败：email：%s", userDetails.getEmail()));
                log.warn(questionAnswer.toString());
                throw new BusinessException(ErrorEnum.ERROR_ANSWER_TOKEN);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.ERROR_ANSWER_TOKEN);
        }
    }

    @Override
    public String getAnswer(String questionId) {
        return examQuestionDao.findById(questionId).orElseGet(ExamQuestion::new).getQuestionAnswer();
    }
}
