package com.up9e.aichat.service.impl;

import com.up9e.aichat.constant.Constant;
import com.up9e.aichat.constant.ErrorEnum;
import com.up9e.aichat.dao.AIChatHistoryDao;
import com.up9e.aichat.dao.AIChatInfoDao;
import com.up9e.aichat.dto.ChatCompletion;
import com.up9e.aichat.entity.AIChatHistory;
import com.up9e.aichat.entity.AIChatInfo;
import com.up9e.aichat.global.BusinessException;
import com.up9e.aichat.recode.ChatMessage;
import com.up9e.aichat.recode.ChatRequest;
import com.up9e.aichat.service.AIChatService;
import com.up9e.aichat.util.EncryptUtils;
import com.up9e.aichat.util.RedisUtils;
import com.up9e.aichat.util.SnowFlakeUtils;
import io.micrometer.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@Log4j2
public class AIChatServiceImpl implements AIChatService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final AIChatInfoDao aiChatInfoDao;

    private final AIChatHistoryDao aiChatHistoryDao;

    private final String openAIKey;

    public AIChatServiceImpl(@Value("${openai.security.key}") String openAIKey,
                             AIChatInfoDao aiChatInfoDao, AIChatHistoryDao aiChatHistoryDao) {
        this.openAIKey = openAIKey;
        this.aiChatInfoDao = aiChatInfoDao;
        this.aiChatHistoryDao = aiChatHistoryDao;
    }

    @Override
    public Pair<String, String> generateTokenAndId(String email) throws BusinessException {
        String chatId = SnowFlakeUtils.nextId();
        String tokenKey = email + Constant.STR_AI + chatId;
        String token = RedisUtils.get(tokenKey);
        if (StringUtils.isEmpty(token)) {
            try {
                token = EncryptUtils.getSecretKey(256);
            } catch (NoSuchAlgorithmException e) {
                throw new BusinessException(ErrorEnum.ERROR_GENERATE_KEY);
            }
            RedisUtils.set(tokenKey, token);
        }
        RedisUtils.expire(tokenKey, 2, TimeUnit.HOURS);
        return Pair.of(token, chatId);
    }

    @Override
    public Map<String, String> withChatGPT(String email, ChatRequest chatRequest, String userId) throws BusinessException {
        String tokenKey = email + Constant.STR_AI + chatRequest.getChatId();
        if (!RedisUtils.get(tokenKey).equals(chatRequest.getToken())) {
            throw new BusinessException(ErrorEnum.ERROR_TOKEN);
        }
        RedisUtils.expire(tokenKey, 2, TimeUnit.HOURS);
        List<ChatMessage> chatMessages = chatRequest.getChatMessages();
        ChatMessage chatMessage = chatMessages.get(chatMessages.size() - 1);
        Optional<AIChatInfo> byId = aiChatInfoDao.findById(chatRequest.getChatId());
        if (byId.isEmpty()) {
            AIChatInfo aiChatInfo = new AIChatInfo();
            aiChatInfo.setChatId(chatRequest.getChatId());
            aiChatInfo.setEmail(email);
            aiChatInfo.setSummary(chatMessage.getContent());
            aiChatInfo.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            aiChatInfo.setCreateUser(email);
            aiChatInfo.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
            aiChatInfo.setUpdateUser(email);
            aiChatInfoDao.saveAndFlush(aiChatInfo);
        }
        AIChatHistory aiChatHistory = new AIChatHistory();
        aiChatHistory.setHistoryId(SnowFlakeUtils.nextId());
        aiChatHistory.setChatId(chatRequest.getChatId());
        aiChatHistory.setRole(chatMessage.getRole());
        aiChatHistory.setContent(chatMessage.getContent());
        aiChatHistory.setCreateUser(email);
        aiChatHistory.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        aiChatHistoryDao.saveAndFlush(aiChatHistory);
        String url = "https://api.openai.com/v1/chat/completions";
        Map<String, Object> postBody = new HashMap<>();
        postBody.put("model", "gpt-3.5-turbo");
        postBody.put("messages", chatRequest.getChatMessages());
        postBody.put("temperature", 0.8);
        postBody.put("user", userId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + openAIKey);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(postBody, headers);
        try {
            ResponseEntity<ChatCompletion> responseEntity = restTemplate.postForEntity(url, entity, ChatCompletion.class);
            ChatCompletion body = responseEntity.getBody();
            if (Optional.ofNullable(body).isEmpty()) {
                throw new BusinessException(ErrorEnum.ERROR_TYPE);
            }
            String role = body.getChoices().get(0).getMessage().getRole();
            String content = body.getChoices().get(0).getMessage().getContent();
            aiChatHistory = new AIChatHistory();
            aiChatHistory.setHistoryId(SnowFlakeUtils.nextId());
            aiChatHistory.setChatId(chatRequest.getChatId());
            aiChatHistory.setRole(role);
            aiChatHistory.setContent(content);
            aiChatHistory.setCreateUser(email);
            aiChatHistory.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            aiChatHistoryDao.saveAndFlush(aiChatHistory);
            return new HashMap<>(2) {{
                put("role", role);
                put("content", content);
            }};
        } catch (Exception ex) {
            if (ex instanceof BusinessException) {
                throw ex;
            } else {
                log.error(ex.getMessage(), ex);
                throw new BusinessException(ErrorEnum.ERROR_TYPE);
            }
        }
    }
}
