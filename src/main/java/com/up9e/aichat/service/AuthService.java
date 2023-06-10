package com.up9e.aichat.service;

import com.up9e.aichat.entity.User;
import com.up9e.aichat.global.BusinessException;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface AuthService {

    Pair<String, String> register(User user) throws BusinessException;

    void verify(String token, String verifyCode, String keySuffix) throws BusinessException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    void registerProcess(String email) throws BusinessException;

    Pair<String, String> resetPassword(User user) throws BusinessException;

    void verifyResetPasswordProcess(String email, String password) throws BusinessException;

    String resend(String token, Integer type);

    void charge(Integer userId, Integer level, Integer plusMonth) throws BusinessException;

    BigDecimal upgrade(Integer userId, Integer plusMonth) throws BusinessException;
}
