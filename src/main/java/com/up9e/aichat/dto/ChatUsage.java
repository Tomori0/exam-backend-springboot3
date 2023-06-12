package com.up9e.aichat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatUsage {

    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;

    @Override
    public String toString() {
        return "ChatUsage{" +
                "promptTokens=" + promptTokens +
                ", completionTokens=" + completionTokens +
                ", totalTokens=" + totalTokens +
                '}';
    }
}
