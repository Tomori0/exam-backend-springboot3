package com.up9e.aichat.service;

import com.up9e.aichat.entity.User;
import com.up9e.aichat.global.BusinessException;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface AuthService {

    /**
     * 注册前置操作
     * @param user 用户信息User
     * @return token,验证码
     * @throws BusinessException 业务异常
     */
    Pair<String, String> register(User user) throws BusinessException;

    /**
     * 重置密码前置操作
     * @param user 用户信息User
     * @return token,验证码
     * @throws BusinessException 业务异常
     */
    Pair<String, String> resetPassword(User user) throws BusinessException;

    /**
     * 生成验证码
     * @param token token
     * @param type 验证码类型(注册/重置密码)
     * @return 验证码
     */
    String generateVerifyCode(String token, Integer type);

    /**
     * 验证验证码
     * @param token token
     * @param verifyCode 验证码
     * @param keySuffix 后缀
     * @throws BusinessException 业务异常
     */
    void verify(String token, String verifyCode, String keySuffix) throws BusinessException;

    /**
     * 注册用户
     * @param email 邮箱
     * @throws BusinessException 业务异常
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    void registerProcess(String email) throws BusinessException;

    /**
     * 重置密码
     * @param email 邮箱
     * @param password 密码
     * @throws BusinessException 业务异常
     */
    void verifyResetPasswordProcess(String email, String password) throws BusinessException;

    /**
     * 购买会员，续费，升级
     * @param type 功能类型(new/renew/upgrade)
     * @param userId 用户id
     * @param level 会员等级
     * @param plusMonth 月数
     * @throws BusinessException 业务异常
     */
    void charge(String type, Integer userId, Integer level, Integer plusMonth) throws BusinessException;

    /**
     * 会员升级，计算未使用天数差价
     * @param userId 用户id
     * @param plusMonth 月数
     * @return 差价
     * @throws BusinessException 业务异常
     */
    BigDecimal upgrade(Integer userId, Integer plusMonth) throws BusinessException;
}
