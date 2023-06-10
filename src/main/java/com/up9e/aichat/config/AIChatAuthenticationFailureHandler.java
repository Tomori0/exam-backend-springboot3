package com.up9e.aichat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.up9e.aichat.constant.ErrorEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AIChatAuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> result = new HashMap<>();
        result.put("status", ErrorEnum.ERROR_LOGIN.getErrorCode());
        result.put("statusText", ErrorEnum.ERROR_LOGIN.getErrorMessage());
        response.setStatus(200);
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
