package com.up9e.aichat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChatUserLevel {
    private Integer userId;
    private Integer level;
    private Integer month;
    private Date    expireDate;

    @Override
    public String toString() {
        return "ChatUserLevel{" +
                "userId=" + userId +
                ", level=" + level +
                ", month=" + month +
                ", expireDate=" + expireDate +
                '}';
    }
}
