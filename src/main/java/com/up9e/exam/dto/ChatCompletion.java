package com.up9e.exam.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatCompletion {
    private String id;
    private String object;
    private Long created;
    private List<ChatChoice> choices;
    private ChatUsage usage;

    @Override
    public String toString() {
        return "ChatCompletion{" +
                "id='" + id + '\'' +
                ", object='" + object + '\'' +
                ", created=" + created +
                ", choices=" + choices +
                ", usage=" + usage +
                '}';
    }
}
