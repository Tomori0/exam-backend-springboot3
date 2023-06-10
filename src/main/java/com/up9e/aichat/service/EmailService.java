package com.up9e.aichat.service;

public interface EmailService {
    void sendMail(String subject, String email, String verifyString);
}
