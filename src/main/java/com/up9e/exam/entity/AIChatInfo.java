package com.up9e.exam.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "ai_chat_info")
public class AIChatInfo {

    @Id
    private String      chatId;
    private String      email;
    private String      summary;
    private String      createUser;
    private Timestamp   createTime;
    private String      updateUser;
    private Timestamp   updateTime;

    @Override
    public String toString() {
        return "ExamInfo{" +
                "chatId=" + chatId +
                ", email='" + email + '\'' +
                ", summary='" + summary + '\'' +
                ", createUser='" + createUser + '\'' +
                ", createTime=" + createTime +
                ", updateUser='" + updateUser + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
