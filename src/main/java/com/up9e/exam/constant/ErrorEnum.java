package com.up9e.exam.constant;

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
    ERROR_ANSWER_TOKEN(10010, "获取答案失败"),
    ERROR_GENERATE_KEY(10011, "key生成失败"),
    ERROR_GET_EXAM_INFO(10012, "获取考试信息失败"),
    ERROR_ANSWER_SIZE(10013, "答案数量错误"),
    ERROR_CHAT_RESPONSE(10014, "聊天发生了异常，请稍后再试"),
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
