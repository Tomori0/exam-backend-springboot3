package com.up9e.aichat.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.up9e.aichat.constant.Constant;
import com.up9e.aichat.constant.ErrorEnum;
import com.up9e.aichat.dao.AIChatPriceDao;
import com.up9e.aichat.dao.AIChatUserLevelDao;
import com.up9e.aichat.dao.UserDao;
import com.up9e.aichat.entity.AIChatPrice;
import com.up9e.aichat.entity.AIChatUserLevel;
import com.up9e.aichat.entity.User;
import com.up9e.aichat.global.BusinessException;
import com.up9e.aichat.service.AuthService;
import com.up9e.aichat.util.RandomUtils;
import com.up9e.aichat.util.RedisUtils;
import io.micrometer.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final AIChatUserLevelDao aiChatUserLevelDao;
    private final AIChatPriceDao aiChatPriceDao;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserDao userDao, AIChatUserLevelDao aiChatUserLevelDao, AIChatPriceDao aiChatPriceDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.aiChatUserLevelDao = aiChatUserLevelDao;
        this.aiChatPriceDao = aiChatPriceDao;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 注册前置操作
     * @param user 用户信息User
     * @return token,验证码
     * @throws BusinessException 业务异常
     */
    @Override
    public Pair<String, String> register(User user) throws BusinessException {
        User findOneCond = new User();
        findOneCond.setEmail(user.getEmail());
        Example<User> example = Example.of(findOneCond);
        if (userDao.findOne(example).isPresent()) {
            throw new BusinessException(ErrorEnum.ERROR_ALREADY_REGISTER);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            user.setSalt(RandomUtils.getRandomStringWithLowercase(64));
            user.setPassword(passwordEncoder.encode(user.getPassword() + user.getSalt()));
            String userJson = objectMapper.writeValueAsString(user);
            String emailKey = user.getEmail() + Constant.STR_REGISTER;
            RedisUtils.set(emailKey, userJson);
            RedisUtils.expire(emailKey, 30, TimeUnit.MINUTES);
            Base64.Encoder encoder = Base64.getEncoder();
            String token = encoder.encodeToString(user.getEmail().getBytes());
            String verifyString = RandomUtils.getRandomString(6);
            String tokenKey = token + Constant.STR_REGISTER;
            RedisUtils.set(tokenKey, verifyString);
            RedisUtils.expire(tokenKey, 30, TimeUnit.MINUTES);
            return Pair.of(token, verifyString);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorEnum.ERROR_PARSE_JSON);
        }
    }

    /**
     * 重置密码前置操作
     * @param user 用户信息User
     * @return token,验证码
     * @throws BusinessException 业务异常
     */
    @Override
    public Pair<String, String> resetPassword(User user) throws BusinessException {
        Example<User> example = Example.of(user);
        if (userDao.findOne(example).isEmpty()) {
            throw new BusinessException(ErrorEnum.ERROR_CANNOT_FIND_USER);
        }
        Base64.Encoder encoder = Base64.getEncoder();
        String token = encoder.encodeToString(user.getEmail().getBytes());
        String verifyString = RandomUtils.getRandomString(6);
        String tokenKey = token + Constant.STR_RESET_PASSWORD;
        RedisUtils.set(tokenKey, verifyString);
        RedisUtils.expire(tokenKey, 30, TimeUnit.MINUTES);
        return Pair.of(token, verifyString);
    }

    /**
     * 生成验证码
     * @param token token
     * @param type 验证码类型(注册/重置密码)
     * @return 验证码
     */
    @Override
    public String generateVerifyCode(String token, Integer type) {
        String verifyString = RandomUtils.getRandomString(6);
        String tokenKey = token;
        if (Constant.INTEGER_TYPE_REGISTER.equals(type)) {
            tokenKey += Constant.STR_REGISTER;
        } else if (Constant.INTEGER_TYPE_RESET_PASSWORD.equals(type)) {
            tokenKey += Constant.STR_RESET_PASSWORD;
        }
        RedisUtils.set(tokenKey, verifyString);
        RedisUtils.expire(tokenKey, 30, TimeUnit.MINUTES);
        return verifyString;
    }

    /**
     * 验证验证码
     * @param token token
     * @param verifyCode 验证码
     * @param keySuffix 后缀
     * @throws BusinessException 业务异常
     */
    @Override
    public void verify(String token, String verifyCode, String keySuffix) throws BusinessException {
        String tokenKey = token + keySuffix;
        String redisVerifyCode = RedisUtils.get(tokenKey);
        if (StringUtils.isEmpty(redisVerifyCode)) {
            throw new BusinessException(ErrorEnum.ERROR_VERIFY_CODE_EXPIRED);
        }
        if (!verifyCode.equals(redisVerifyCode)) {
            throw new BusinessException(ErrorEnum.ERROR_VERIFY_CODE_ERROR);
        }
        RedisUtils.delete(tokenKey);
    }

    /**
     * 注册用户
     * @param email 邮箱
     * @throws BusinessException 业务异常
     */
    @Override
    public void registerProcess(String email) throws BusinessException {
        String emailKey = email + Constant.STR_REGISTER;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            User user = objectMapper.readValue(RedisUtils.get(emailKey), User.class);
            user.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            user.setLastLoginTime(Timestamp.valueOf(LocalDateTime.now()));
            Integer userId = userDao.saveAndFlush(user).getId();
            RedisUtils.delete(emailKey);
            // AIChat登录 新用户免费试用7天 & 3.5版本
            AIChatUserLevel aiChatUserLevel = new AIChatUserLevel();
            aiChatUserLevel.setUserId(userId);
            aiChatUserLevel.setLevel(Constant.INTEGER_CHAT_LEVEL_DEMO);
            aiChatUserLevel.setExpireDate(Date.valueOf(LocalDate.now().plusDays(Constant.INTEGER_CHAT_PLUD_DAY_7.longValue())));
            aiChatUserLevel.setCreateUser(Constant.STR_REGISTER);
            aiChatUserLevel.setCreateTime(Timestamp.valueOf((LocalDateTime.now())));
            aiChatUserLevel.setUpdateUser(Constant.STR_REGISTER);
            aiChatUserLevel.setUpdateTime(Timestamp.valueOf((LocalDateTime.now())));
            aiChatUserLevel.setDeleteFlg(Constant.STR_DELETEFLG_0);
            aiChatUserLevelDao.saveAndFlush(aiChatUserLevel);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorEnum.ERROR_PARSE_JSON);
        }
    }

    /**
     * 重置密码
     * @param email 邮箱
     * @param password 密码
     * @throws BusinessException 业务异常
     */
    @Override
    public void verifyResetPasswordProcess(String email, String password) throws BusinessException {
        User user = new User();
        user.setEmail(email);
        Optional<User> examUserOptional = userDao.findOne(Example.of(user));
        if (examUserOptional.isEmpty()) {
            throw new BusinessException(ErrorEnum.ERROR_CANNOT_FIND_USER);
        }
        User afterUser = examUserOptional.get();
        String salt = RandomUtils.getRandomStringWithLowercase(64);
        afterUser.setPassword(passwordEncoder.encode(password + salt));
        afterUser.setSalt(salt);
        userDao.saveAndFlush(afterUser);
    }

    /**
     * 购买会员，续费，升级
     * @param type 功能类型(new/renew/upgrade)
     * @param userId 用户id
     * @param level 会员等级
     * @param plusMonth 月数
     * @throws BusinessException 业务异常
     */
    @Override
    public void charge(String type, Integer userId, Integer level, Integer plusMonth) throws BusinessException{
        AIChatUserLevel findAIChatUserLevel = new AIChatUserLevel();
        findAIChatUserLevel.setUserId(userId);
        findAIChatUserLevel.setDeleteFlg(Constant.STR_DELETEFLG_0);
        Optional<AIChatUserLevel> foundAIChatUserLevel = aiChatUserLevelDao.findOne(Example.of(findAIChatUserLevel));
        if (foundAIChatUserLevel.isEmpty()) {
            throw new BusinessException(ErrorEnum.ERROR_CANNOT_FIND_USER_LEVEL);
        }
        AIChatUserLevel aiChatUserLevel = foundAIChatUserLevel.get();
        aiChatUserLevel.setLevel(level);
        if (type.equals(Constant.STR_AI_NEW) || type.equals(Constant.STR_AI_RENEW)){
            aiChatUserLevel.setMonth(plusMonth);
            LocalDate oldExpireDate = aiChatUserLevel.getExpireDate().toLocalDate();
            aiChatUserLevel.setExpireDate(Date.valueOf(oldExpireDate.plusDays(plusMonth.longValue() * Constant.INTEGER_CHAT_DAY_OF_ONE_MONTH)));
        }
        aiChatUserLevel.setUpdateUser(Constant.STR_AI_CHARGE);
        aiChatUserLevel.setUpdateTime(Timestamp.valueOf(LocalDate.now().toString()));
        aiChatUserLevelDao.saveAndFlush(aiChatUserLevel);
    }

    /**
     * 会员升级，计算未使用天数差价
     * @param userId 用户id
     * @param plusMonth 月数
     * @return 差价
     * @throws BusinessException 业务异常
     */
    @Override
    public BigDecimal upgrade(Integer userId, Integer plusMonth) throws BusinessException{
        BigDecimal priceDiff = BigDecimal.ZERO;
        AIChatUserLevel findAIChatUserLevel = new AIChatUserLevel();
        findAIChatUserLevel.setUserId(userId);
        findAIChatUserLevel.setDeleteFlg(Constant.STR_DELETEFLG_0);
        Optional<AIChatUserLevel> foundAIChatUserLevel = aiChatUserLevelDao.findOne(Example.of(findAIChatUserLevel));
        if (foundAIChatUserLevel.isEmpty()) {
            throw new BusinessException(ErrorEnum.ERROR_CANNOT_FIND_USER_LEVEL);
        }
        AIChatUserLevel aiChatUserLevel = foundAIChatUserLevel.get();
        int level = aiChatUserLevel.getLevel();
        if (level == Constant.INTEGER_CHAT_LEVEL_DEMO){
            throw new BusinessException(ErrorEnum.ERROR_CANNOT_UPGRADE_LEVEL_FREE);
        } else if (level == Constant.INTEGER_CHAT_LEVEL_PLUS) {
            throw new BusinessException(ErrorEnum.ERROR_CANNOT_UPGRADE_LEVEL_PLUS);
        } else {
            // 未使用天数计算(升级当日不算)
            LocalDate expireDate = aiChatUserLevel.getExpireDate().toLocalDate();
            long dayDiff = (Date.valueOf(expireDate.plusDays(1)).getTime() - Date.valueOf(LocalDate.now()).getTime()) / (24 * 60 * 60 * 1000);
            // 差价计算
            BigDecimal priceLevel1 = new BigDecimal(0);
            BigDecimal priceLevel2 = new BigDecimal(0);
            List<AIChatPrice> aiChatPrices = aiChatPriceDao.findAllByMonth(plusMonth);
            if (aiChatPrices.isEmpty()){
                throw new BusinessException(ErrorEnum.ERROR_FIND_CHAT_PRICE);
            }
            for (AIChatPrice aiChatPrice :aiChatPrices){
                if (aiChatPrice.getLevel().equals(Constant.INTEGER_CHAT_LEVEL_FREE)){
                    priceLevel1 = aiChatPrice.getPrice();
                } else {
                    priceLevel2 = aiChatPrice.getPrice();
                }
            }
            priceDiff = (priceLevel2.subtract(priceLevel1))
                    .divide(BigDecimal.valueOf(Constant.INTEGER_CHAT_DAY_OF_ONE_MONTH), RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(dayDiff));
        }
        return priceDiff;
    }
}