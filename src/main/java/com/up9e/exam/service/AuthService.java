package com.up9e.exam.service;

import com.up9e.exam.entity.ExamUser;
import com.up9e.exam.global.BusinessException;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    public Pair<String, String> register(ExamUser user) throws BusinessException;

    void verify(String token, String verifyCode, String keySuffix) throws BusinessException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    void registerProcess(String email) throws BusinessException;

    Pair<String, String> resetPassword(ExamUser user) throws BusinessException;

    void verifyResetPasswordProcess(String email, String password) throws BusinessException;

    String resend(String token, Integer type);
}
