package com.up9e.exam.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
public class ExamInfoWithToken {

    private String      infoId;
    private String      examCode;
    private String      examTitle;
    private Integer     categoryId;
    private String      examLogo;
    private Integer     totalScore;
    private Time        expireTime;
    private String      createUser;
    private Timestamp   createTime;
    private String      updateUser;
    private Timestamp   updateTime;
    private String      token;

}
