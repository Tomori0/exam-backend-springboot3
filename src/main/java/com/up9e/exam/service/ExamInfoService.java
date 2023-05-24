package com.up9e.exam.service;

import com.up9e.exam.dto.ExamInfoWithCategory;
import com.up9e.exam.dto.ExamInfoWithToken;
import com.up9e.exam.global.BusinessException;

import java.util.List;

public interface ExamInfoService {

    List<ExamInfoWithCategory> getInfoWithCategory();

    ExamInfoWithToken getExamInfoByInfoId(String infoId, String email) throws BusinessException;
}
