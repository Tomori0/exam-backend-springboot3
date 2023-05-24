package com.up9e.exam.controller;

import com.up9e.exam.dto.ExamInfoWithCategory;
import com.up9e.exam.global.ResponseApi;
import com.up9e.exam.service.PractiseInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/practise/info")
public class PractiseInfoController {

    private final PractiseInfoService practiseInfoService;

    public PractiseInfoController(PractiseInfoService practiseInfoService) {
        this.practiseInfoService = practiseInfoService;
    }

    @GetMapping("/list")
    public ResponseApi<List<ExamInfoWithCategory>> list() {
        return new ResponseApi<>(practiseInfoService.getInfoWithCategory());
    }
}
