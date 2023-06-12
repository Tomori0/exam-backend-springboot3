package com.up9e.aichat.service;

import com.up9e.aichat.global.BusinessException;
import com.up9e.aichat.recode.ChatRequest;
import org.springframework.data.util.Pair;

import java.util.Map;

public interface AIChatService {

    /**
     * 生成token,chatId
     * @param email 邮箱
     * @return token,chatId
     * @throws BusinessException
     */
    Pair<String, String> generateTokenAndId(String email) throws BusinessException;

    /**
     * ChatGPT交互
     * @param email 邮箱
     * @param chatRequest chat请求
     * @param userId 用户id
     * @return 角色,内容
     * @throws BusinessException
     */
    Map<String, String> withChatGPT(String email, ChatRequest chatRequest, String userId) throws BusinessException;
}
