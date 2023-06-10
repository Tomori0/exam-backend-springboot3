package com.up9e.aichat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "ai_chat_user_level", schema = "public")
public class AIChatUserLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer     id;
    private Integer     userId;
    private Integer     level;
    private Integer     month;
    private Date        expireDate;
    private String      createUser;
    private Timestamp   createTime;
    private String      updateUser;
    private Timestamp   updateTime;
    private String      deleteFlg;

    @Override
    public String toString() {
        return "AIChatUserLevel{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", level='" + level + '\'' +
                ", month='" + month + '\'' +
                ", expireDate=" + expireDate +
                ", createUser='" + createUser + '\'' +
                ", createTime='" + createTime +
                ", updateUser='" + updateUser + '\'' +
                ", updateTime=" + updateTime +
                ", deleteFlg=" + deleteFlg +
                '}';
    }
}
