package com.up9e.exam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "exam_question")
public class ExamQuestion {

    @Id
    private String      questionId;
    private String      infoId;
    private String      questionHead;
    private String      questionImg;
    private Integer     questionType;
    private String      questionBody;
    private String      questionAnswer;
    private Integer     questionScore;
    private String      createUser;
    private Timestamp   createTime;
    private String      updateUser;
    private Timestamp   updateTime;

    @Override
    public String toString() {
        return "ExamQuestion{" +
                "questionId=" + questionId +
                ", infoId=" + infoId +
                ", questionHead='" + questionHead + '\'' +
                ", questionImg='" + questionImg + '\'' +
                ", questionType=" + questionType +
                ", questionBody='" + questionBody + '\'' +
                ", questionAnswer='" + questionAnswer + '\'' +
                ", questionScore=" + questionScore +
                ", createUser='" + createUser + '\'' +
                ", createTime=" + createTime +
                ", updateUser='" + updateUser + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
