package com.up9e.exam.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.up9e.exam.global.ResponseApi;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class ExamAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(401);
        response.getWriter().print(objectMapper.writeValueAsString(new ResponseApi<>(10001, "需要先认证才能访问!")));
    }
}
