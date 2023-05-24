package com.up9e.exam.recode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    private String role;
    private String content;

    @Override
    public String toString() {
        return "ChatMessage{" +
                "role='" + role + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
