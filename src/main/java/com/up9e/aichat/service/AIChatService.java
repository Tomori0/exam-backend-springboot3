package com.up9e.aichat.service;

import com.up9e.aichat.global.BusinessException;
import com.up9e.aichat.recode.ChatRequest;
import org.springframework.data.util.Pair;

import java.util.Map;

public interface AIChatService {

    Pair<String, String> generateTokenAndId(String email) throws BusinessException;

    Map<String, String> withChatGPT(String email, ChatRequest chatRequest, String userId) throws BusinessException;
}
