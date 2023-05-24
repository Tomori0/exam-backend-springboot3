package com.up9e.exam.service;

import com.up9e.exam.global.BusinessException;
import com.up9e.exam.recode.ChatRequest;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;

public interface AIChatService {

    Pair<String, String> generateTokenAndId(String email) throws BusinessException;

    Map<String, String> withChatGPT(String email, ChatRequest chatRequest, String userId) throws BusinessException;
}
