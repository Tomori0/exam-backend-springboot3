package com.up9e.exam.global;

import com.up9e.exam.constant.ErrorEnum;

public class BusinessException extends Exception {

    private final ErrorEnum errorEnum;

    public BusinessException(ErrorEnum errorEnum) {
        super(errorEnum.toString());
        this.errorEnum = errorEnum;
    }

    public ErrorEnum getErrorEnum() {
        return errorEnum;
    }
}
