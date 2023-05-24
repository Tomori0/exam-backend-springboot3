package com.up9e.exam.service.impl;

import com.up9e.exam.constant.Constant;
import com.up9e.exam.constant.ErrorEnum;
import com.up9e.exam.dao.ExamCategoryDao;
import com.up9e.exam.dao.ExamInfoDao;
import com.up9e.exam.dto.ExamInfoWithCategory;
import com.up9e.exam.dto.ExamInfoWithToken;
import com.up9e.exam.entity.ExamCategory;
import com.up9e.exam.entity.ExamInfo;
import com.up9e.exam.global.BusinessException;
import com.up9e.exam.service.ExamInfoService;
import com.up9e.exam.util.EncryptUtils;
import com.up9e.exam.util.RedisUtils;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ExamInfoServiceImpl implements ExamInfoService {

    private final ExamCategoryDao examCategoryDao;

    private final ExamInfoDao examInfoDao;

    public ExamInfoServiceImpl(ExamCategoryDao examCategoryDao, ExamInfoDao examInfoDao) {
        this.examCategoryDao = examCategoryDao;
        this.examInfoDao = examInfoDao;
    }

    @Override
    public List<ExamInfoWithCategory> getInfoWithCategory() {
        List<ExamCategory> allExamCategory = examCategoryDao.findAll();
        List<ExamInfo> allExamInfo = examInfoDao.findByExpireTimeNot(Time.valueOf("0:00:00"));
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

    @Override
    public ExamInfoWithToken getExamInfoByInfoId(String infoId, String email) throws BusinessException {
        Optional<ExamInfo> examInfoOptional = examInfoDao.findById(infoId);
        if (examInfoOptional.isEmpty()) {
            return null;
        }
        ExamInfo examInfo = examInfoOptional.get();
        String tokenKey = email + Constant.STR_EXAM + infoId;
        String token = RedisUtils.get(tokenKey);
        if (StringUtils.isEmpty(token)) {
            try {
                token = EncryptUtils.getSecretKey(256);
            } catch (NoSuchAlgorithmException e) {
                throw new BusinessException(ErrorEnum.ERROR_GENERATE_KEY);
            }
            RedisUtils.set(tokenKey, token);
        }
        RedisUtils.expire(tokenKey, 125, TimeUnit.MINUTES);
        ExamInfoWithToken examInfoWithToken = new ExamInfoWithToken();
        examInfoWithToken.setInfoId(examInfo.getInfoId());
        examInfoWithToken.setExamCode(examInfo.getExamCode());
        examInfoWithToken.setExamTitle(examInfo.getExamTitle());
        examInfoWithToken.setCategoryId(examInfo.getCategoryId());
        examInfoWithToken.setExamLogo(examInfo.getExamLogo());
        examInfoWithToken.setTotalScore(examInfo.getTotalScore());
        examInfoWithToken.setExpireTime(examInfo.getExpireTime());
        examInfoWithToken.setCreateUser(examInfo.getCreateUser());
        examInfoWithToken.setCreateTime(examInfo.getCreateTime());
        examInfoWithToken.setUpdateUser(examInfo.getUpdateUser());
        examInfoWithToken.setUpdateTime(examInfo.getUpdateTime());
        examInfoWithToken.setToken(token);
        return examInfoWithToken;
    }

}
