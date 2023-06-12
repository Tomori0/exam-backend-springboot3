package com.up9e.aichat.service;

import com.up9e.aichat.global.BusinessException;

public interface EmailService {

    /**
     * 发送邮件
     * @param subject 邮件主题
     * @param email 邮箱
     * @param verifyString 验证码
     * @throws BusinessException 业务异常
     */
    void sendMail(String subject, String email, String verifyString) throws BusinessException;
}
