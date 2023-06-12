package com.up9e.aichat.controller;

import com.up9e.aichat.constant.Constant;
import com.up9e.aichat.constant.ErrorEnum;
import com.up9e.aichat.entity.User;
import com.up9e.aichat.global.UserDetails;
import com.up9e.aichat.recode.RegistryVerify;
import com.up9e.aichat.global.BusinessException;
import com.up9e.aichat.global.ResponseApi;
import com.up9e.aichat.recode.ResendToken;
import com.up9e.aichat.recode.ResetPasswordVerify;
import com.up9e.aichat.service.AuthService;
import com.up9e.aichat.service.EmailService;
import com.up9e.aichat.util.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    public AuthController(AuthService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }

    @GetMapping("/info")
    public ResponseApi<User> login(@AuthenticationPrincipal UserDetails userDetails) {
        User user = new User();
        user.setEmail(userDetails.getEmail());
        user.setBirthday(userDetails.getBirthday());
        user.setAvatar(userDetails.getAvatar());
        user.setNickname(userDetails.getNickname());
        user.setCreateTime(userDetails.getCreateTime());
        user.setLastLoginTime(userDetails.getLastLoginTime());
        return new ResponseApi<>(user);
    }

    @PostMapping("/register")
    public ResponseApi<String> register(@RequestBody User user) throws BusinessException {
        Pair<String, String> tokenAndVerifyString = authService.register(user);
        emailService.sendMail("玖义聊注册验证码", user.getEmail(), tokenAndVerifyString.getSecond());
        return new ResponseApi<>(tokenAndVerifyString.getFirst());
    }

    @PostMapping("/verify")
    public ResponseApi<Map<String, String>> register(@RequestBody RegistryVerify registryVerify,
                                                     @Value("${jwt.tokenHead}") String tokenHead,
                                                     HttpServletResponse response) throws BusinessException {
        authService.verify(registryVerify.token(), registryVerify.verifyCode(), Constant.STR_REGISTER);
        authService.registerProcess(registryVerify.email());
        return getLoginResponse(tokenHead, registryVerify.email(), response);
    }

    @PostMapping("/resetPassword")
    public ResponseApi<String> resetPassword(@RequestBody User user) throws BusinessException {
        Pair<String, String> tokenAndVerifyString = authService.resetPassword(user);
        emailService.sendMail("玖义聊重置密码验证码", user.getEmail(), tokenAndVerifyString.getSecond());
        return new ResponseApi<>(tokenAndVerifyString.getFirst());
    }

    @PostMapping("/verifyResetPassword")
    public ResponseApi<Map<String, String>> verifyResetPassword(@RequestBody ResetPasswordVerify resetPasswordVerify,
                                                                @Value("${jwt.tokenHead}") String tokenHead,
                                                                HttpServletResponse response) throws BusinessException {
        authService.verify(resetPasswordVerify.token(), resetPasswordVerify.verifyCode(), Constant.STR_RESET_PASSWORD);
        authService.verifyResetPasswordProcess(resetPasswordVerify.email(), resetPasswordVerify.password());
        return getLoginResponse(tokenHead, resetPasswordVerify.email(), response);
    }

    @PostMapping("/resend")
    public ResponseApi<String> resend(@RequestBody ResendToken resendToken) throws BusinessException {
        String subject;
        if (Constant.INTEGER_TYPE_REGISTER.equals(resendToken.type())) {
            subject = "玖义聊注册验证码";
        } else if (Constant.INTEGER_TYPE_RESET_PASSWORD.equals(resendToken.type())) {
            subject = "玖义聊重置密码验证码";
        } else {
            throw new BusinessException(ErrorEnum.ERROR_TYPE);
        }
        String verifyToken = authService.generateVerifyCode(resendToken.token(), resendToken.type());
        emailService.sendMail(subject, resendToken.email(), verifyToken);
        return new ResponseApi<>("验证码发送成功");
    }

    private ResponseApi<Map<String, String>> getLoginResponse(String tokenHead, String email,
                                                              HttpServletResponse response) throws BusinessException {
        response.setContentType("application/json");
        Map<String, String> result = new HashMap<>();
        result.put("tokenHead", tokenHead);
        result.put("token", JwtUtils.generateToken(email));
        return new ResponseApi<>(result);
    }
}