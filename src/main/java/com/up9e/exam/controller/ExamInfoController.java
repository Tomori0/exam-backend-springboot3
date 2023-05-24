package com.up9e.exam.controller;

import com.up9e.exam.constant.ErrorEnum;
import com.up9e.exam.dto.ExamInfoWithCategory;
import com.up9e.exam.dto.ExamInfoWithToken;
import com.up9e.exam.global.BusinessException;
import com.up9e.exam.global.ExamUserDetails;
import com.up9e.exam.global.ResponseApi;
import com.up9e.exam.service.ExamInfoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exam/info")
public class ExamInfoController {

    private final ExamInfoService examInfoService;

    public ExamInfoController(ExamInfoService examInfoService) {
        this.examInfoService = examInfoService;
    }

    @GetMapping("/list")
    public ResponseApi<List<ExamInfoWithCategory>> list() {
        return new ResponseApi<>(examInfoService.getInfoWithCategory());
    }

    @GetMapping("/get")
    public ResponseApi<ExamInfoWithToken> get(@AuthenticationPrincipal ExamUserDetails userDetails,
                                              @RequestParam String infoId) throws BusinessException {
        ExamInfoWithToken examInfo = examInfoService.getExamInfoByInfoId(infoId, userDetails.getEmail());
        if (examInfo == null) {
            throw new BusinessException(ErrorEnum.ERROR_GET_EXAM_INFO);
        }
        return new ResponseApi<>(examInfo);
    }
}
