package com.up9e.exam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "ai_chat_history")
public class AIChatHistory {

    @Id
    private String      historyId;
    private String      chatId;
    private String      role;
    private String      content;
    private String      createUser;
    private Timestamp   createTime;

    @Override
    public String toString() {
        return "ExamInfo{" +
                "historyId=" + historyId +
                ", chatId='" + chatId + '\'' +
                ", role='" + role + '\'' +
                ", content='" + content + '\'' +
                ", createUser='" + createUser + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
