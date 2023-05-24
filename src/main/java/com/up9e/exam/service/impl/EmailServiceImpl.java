package com.up9e.exam.service.impl;

import com.up9e.exam.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final String from;

    public EmailServiceImpl(JavaMailSender javaMailSender, @Value("${spring.mail.username}") String from) {
        this.javaMailSender = javaMailSender;
        this.from = from;
    }

    @Async("taskExecutor")
    @Override
    public void sendMail(String subject, String email, String verifyString) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(from);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText("您的验证码为：" + verifyString, true);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
        javaMailSender.send(helper.getMimeMessage());
    }
}
