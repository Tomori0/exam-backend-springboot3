package com.up9e.aichat.constant;

public enum ErrorEnum {
    ERROR_TOKEN(10001, "token异常，请重新登录"),
    ERROR_SEND_MAIL(10002, "邮件发送失败"),
    ERROR_PARSE_JSON(10003, "转换json失败"),
    ERROR_ALREADY_REGISTER(10004, "该邮箱已经注册，请登录或更换邮箱"),
    ERROR_VERIFY_CODE_EXPIRED(10005, "验证码已失效"),
    ERROR_VERIFY_CODE_ERROR(10006, "验证码错误"),
    ERROR_LOGIN(10007, "用户名或密码错误"),
    ERROR_CANNOT_FIND_USER(10008, "用户不存在"),
    ERROR_TYPE(10009, "类型错误"),
    ERROR_GENERATE_KEY(10010, "key生成失败"),
    ERROR_CANNOT_FIND_USER_LEVEL(10011, "用户AIChat信息不存在"),
    ERROR_CANNOT_UPGRADE_LEVEL_FREE(10012, "您现在处于试用服务期，无法升级，如有需要请购买会员服务"),
    ERROR_CANNOT_UPGRADE_LEVEL_PLUS(10013, "您现在使用的已是最高版本，无需升级"),
    ERROR_FIND_CHAT_PRICE(10014, "数据出错，请将报错信息反馈给管理员"),
    ERROR_CLOCK_BACK(50001, "时钟回退");
    private final Integer errorCode;
    private final String errorMessage;

    ErrorEnum(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "errorCode=" + errorCode + ", errorMessage='" + errorMessage;
    }
}
