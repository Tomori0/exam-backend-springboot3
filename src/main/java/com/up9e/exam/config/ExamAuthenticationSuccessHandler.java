package com.up9e.exam.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.up9e.exam.global.ExamUserDetails;
import com.up9e.exam.util.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ExamAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final String tokenHead;

    public ExamAuthenticationSuccessHandler(@Value("${jwt.tokenHead}") String tokenHead) {
        this.tokenHead = tokenHead;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        ExamUserDetails principal = (ExamUserDetails) auth.getPrincipal();
        String token = JwtUtils.generateToken(principal.getEmail());
        response.setContentType("application/json");
        Map<String, Object> result = new HashMap<>();
        result.put("status", 200);
        result.put("data", new HashMap<String, String>() {{
            put("tokenHead", tokenHead);
            put("token", token);
        }});
        response.setStatus(200);
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
