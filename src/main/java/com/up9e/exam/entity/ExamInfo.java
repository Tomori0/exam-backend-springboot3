package com.up9e.exam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "exam_info")
public class ExamInfo {

    @Id
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

    @Override
    public String toString() {
        return "ExamInfo{" +
                "infoId=" + infoId +
                ", examCode='" + examCode + '\'' +
                ", examTitle='" + examTitle + '\'' +
                ", categoryId=" + categoryId +
                ", examLogo='" + examLogo + '\'' +
                ", totalScore=" + totalScore +
                ", expireTime=" + expireTime +
                ", createUser='" + createUser + '\'' +
                ", createTime=" + createTime +
                ", updateUser='" + updateUser + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
