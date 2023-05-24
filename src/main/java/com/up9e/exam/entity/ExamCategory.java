package com.up9e.exam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "exam_category")
public class ExamCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer     categoryId;
    private String      categoryName;
    private String      createUser;
    private Timestamp   createTime;
    private String      updateUser;
    private Timestamp   updateTime;

    @Override
    public String toString() {
        return "ExamCategory{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", createUser='" + createUser + '\'' +
                ", createTime=" + createTime +
                ", updateUser='" + updateUser + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
