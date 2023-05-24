package com.up9e.exam.recode;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRequest {

    private String chatId;
    private String token;
    private List<ChatMessage> chatMessages;

    @Override
    public String toString() {
        return "ChatRequest{" +
                "chatId='" + chatId + '\'' +
                ", token='" + token + '\'' +
                ", chatMessages=" + chatMessages +
                '}';
    }
}
