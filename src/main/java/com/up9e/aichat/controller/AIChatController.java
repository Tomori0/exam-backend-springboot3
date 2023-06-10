package com.up9e.aichat.controller;

import com.up9e.aichat.global.BusinessException;
import com.up9e.aichat.global.UserDetails;
import com.up9e.aichat.global.ResponseApi;
import com.up9e.aichat.recode.ChatRequest;
import com.up9e.aichat.service.AIChatService;
import org.springframework.data.util.Pair;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai/chat")
public class AIChatController {

    private final AIChatService aiChatService;

    public AIChatController(AIChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    public ResponseApi<Map<String, String>> addUser(@AuthenticationPrincipal UserDetails userDetails) throws BusinessException {
        return null;
    }

    @GetMapping("/getToken")
    public ResponseApi<Map<String, String>> getToken(@AuthenticationPrincipal UserDetails userDetails) throws BusinessException {
        Pair<String, String> pair = aiChatService.generateTokenAndId(userDetails.getEmail());
        String token = pair.getFirst();
        String chatId = pair.getSecond();
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("token", token);
        returnMap.put("chatId", chatId);
        return new ResponseApi<>(returnMap);
    }

    @PostMapping("/completions")
    public ResponseApi<Map<String, String>> completions(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody ChatRequest chatRequest) throws BusinessException {
        return new ResponseApi<>(aiChatService.withChatGPT(userDetails.getEmail(), chatRequest, String.valueOf(userDetails.getId())));
    }
}
