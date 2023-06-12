package com.up9e.aichat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatChoice {

    private Integer index;
    private ChatMessage message;
    private String finishReason;

    @Override
    public String toString() {
        return "ChatChoice{" +
                "index=" + index +
                ", message=" + message +
                ", finishReason='" + finishReason + '\'' +
                '}';
    }
}
