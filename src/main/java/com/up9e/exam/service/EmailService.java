package com.up9e.exam.service;

public interface EmailService {
    void sendMail(String subject, String email, String verifyString);
}
