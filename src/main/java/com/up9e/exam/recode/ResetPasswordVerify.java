package com.up9e.exam.recode;

public record ResetPasswordVerify(String email, String password, String token, String verifyCode) {

}
