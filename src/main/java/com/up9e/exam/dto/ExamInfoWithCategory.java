package com.up9e.exam.dto;

import com.up9e.exam.entity.ExamInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamInfoWithCategory {
    private Integer categoryId;
    private String categoryName;
    private List<ExamInfo> infoList;

}
