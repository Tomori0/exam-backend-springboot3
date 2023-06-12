package com.up9e.aichat.controller;

import com.up9e.aichat.constant.ErrorEnum;
import com.up9e.aichat.global.BusinessException;
import com.up9e.aichat.global.ResponseApi;
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
