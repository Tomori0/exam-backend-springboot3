package com.up9e.exam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "exam_user")
public class ExamUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer     id;
    private String      email;
    private String      nickname;
    private Date        birthday;
    private String      password;
    private String      avatar;
    private String      salt;
    private Timestamp   lastLoginTime;
    private Timestamp   createTime;

    @Override
    public String toString() {
        return "ExamUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", birthday=" + birthday +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", salt='" + salt + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", createTime=" + createTime +
                '}';
    }
}
