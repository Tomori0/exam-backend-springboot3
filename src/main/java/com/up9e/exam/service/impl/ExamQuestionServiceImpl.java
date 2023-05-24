package com.up9e.exam.service.impl;

import com.up9e.exam.constant.Constant;
import com.up9e.exam.constant.ErrorEnum;
import com.up9e.exam.dao.ExamQuestionDao;
import com.up9e.exam.entity.ExamQuestion;
import com.up9e.exam.global.BusinessException;
import com.up9e.exam.service.ExamQuestionService;
import com.up9e.exam.util.EncryptUtils;
import com.up9e.exam.util.RedisUtils;
import io.micrometer.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@Log4j2
public class ExamQuestionServiceImpl implements ExamQuestionService {

    private final ExamQuestionDao examQuestionDao;

    public ExamQuestionServiceImpl(ExamQuestionDao examQuestionDao) {
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
    public List<String> verifyAnswer(String infoId, List<String> answerList, String email, Date timestamp, String verify) throws BusinessException {
        String tokenKey = email + Constant.STR_EXAM + infoId;
        String token = RedisUtils.get(tokenKey);
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(ErrorEnum.ERROR_ANSWER_TOKEN);
        }
        String message = infoId + timestamp.getTime() + token + answerList.toString();
        try {
            String encoded = EncryptUtils.encode(token, message.replace(" ", ""));
            if (!encoded.equals(verify)) {
                log.warn(String.format("验签失败：email：%s", email));
                throw new BusinessException(ErrorEnum.ERROR_ANSWER_TOKEN);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.ERROR_ANSWER_TOKEN);
        }
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setInfoId(infoId);
        List<ExamQuestion> examQuestionList = examQuestionDao.findAll(Example.of(examQuestion), Sort.by(Sort.Direction.ASC, "questionId"));
        if (examQuestionList.size() != answerList.size()) {
            throw new BusinessException(ErrorEnum.ERROR_ANSWER_SIZE);
        }
        List<String> answerReturn = new LinkedList<>();
        for (int i = 0; i < examQuestionList.size(); i++) {
            if (!examQuestionList.get(i).getQuestionAnswer().equals(answerList.get(i))) {
                answerReturn.add(examQuestionList.get(i).getQuestionAnswer());
            } else {
                answerReturn.add(Strings.EMPTY);
            }
        }
        return answerReturn;
    }

}
