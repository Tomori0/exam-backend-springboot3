package com.up9e.exam.controller;

import com.up9e.exam.constant.ErrorEnum;
import com.up9e.exam.global.BusinessException;
import com.up9e.exam.global.ResponseApi;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value = BusinessException.class)
    public ResponseApi<Object> businessException(BusinessException e) {
        ErrorEnum errorEnum = e.getErrorEnum();
        return new ResponseApi<>(errorEnum.getErrorCode(), errorEnum.getErrorMessage());
    }

}
