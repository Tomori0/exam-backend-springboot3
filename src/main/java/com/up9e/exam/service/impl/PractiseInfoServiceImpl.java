package com.up9e.exam.service.impl;

import com.up9e.exam.dao.ExamCategoryDao;
import com.up9e.exam.dao.ExamInfoDao;
import com.up9e.exam.dto.ExamInfoWithCategory;
import com.up9e.exam.entity.ExamCategory;
import com.up9e.exam.entity.ExamInfo;
import com.up9e.exam.service.PractiseInfoService;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PractiseInfoServiceImpl implements PractiseInfoService {

    private final ExamCategoryDao examCategoryDao;

    private final ExamInfoDao examInfoDao;

    public PractiseInfoServiceImpl(ExamCategoryDao examCategoryDao, ExamInfoDao examInfoDao) {
        this.examCategoryDao = examCategoryDao;
        this.examInfoDao = examInfoDao;
    }

    @Override
    public List<ExamInfoWithCategory> getInfoWithCategory() {
        List<ExamCategory> allExamCategory = examCategoryDao.findAll();
        List<ExamInfo> allExamInfo = examInfoDao.findByExpireTime(Time.valueOf("0:00:00"));
        List<ExamInfoWithCategory> examInfoWithCategoryList = new LinkedList<>();
        Map<Integer, List<ExamInfo>> examInfoMap = allExamInfo.stream().collect(Collectors.groupingBy(ExamInfo::getCategoryId));
        allExamCategory.forEach(examCategory -> {
            List<ExamInfo> examInfoList = examInfoMap.get(examCategory.getCategoryId());
            if (examInfoList != null) {
                ExamInfoWithCategory examInfoWithCategory = new ExamInfoWithCategory();
                examInfoWithCategory.setCategoryId(examCategory.getCategoryId());
                examInfoWithCategory.setCategoryName(examCategory.getCategoryName());
                examInfoWithCategory.setInfoList(examInfoList);
                examInfoWithCategoryList.add(examInfoWithCategory);
            }
        });
        return examInfoWithCategoryList;
    }

}
