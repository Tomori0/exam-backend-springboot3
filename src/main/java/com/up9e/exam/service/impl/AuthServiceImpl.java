package com.up9e.exam.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.up9e.exam.constant.Constant;
import com.up9e.exam.constant.ErrorEnum;
import com.up9e.exam.dao.ExamUserDao;
import com.up9e.exam.entity.ExamUser;
import com.up9e.exam.global.BusinessException;
import com.up9e.exam.service.AuthService;
import com.up9e.exam.util.RandomUtils;
import com.up9e.exam.util.RedisUtils;
import io.micrometer.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class AuthServiceImpl implements AuthService {

    private final ExamUserDao examUserDao;

    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(ExamUserDao examUserDao, PasswordEncoder passwordEncoder) {
        this.examUserDao = examUserDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Pair<String, String> register(ExamUser user) throws BusinessException {
        ExamUser findOneCond = new ExamUser();
        findOneCond.setEmail(user.getEmail());
        Example<ExamUser> example = Example.of(findOneCond);
        if (examUserDao.findOne(example).isPresent()) {
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

    @Override
    public void registerProcess(String email) throws BusinessException {
        String emailKey = email + Constant.STR_REGISTER;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ExamUser examUser = objectMapper.readValue(RedisUtils.get(emailKey), ExamUser.class);
            examUser.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            examUser.setLastLoginTime(Timestamp.valueOf(LocalDateTime.now()));
            examUserDao.saveAndFlush(examUser);
            RedisUtils.delete(emailKey);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorEnum.ERROR_PARSE_JSON);
        }
    }

    @Override
    public Pair<String, String> resetPassword(ExamUser user) throws BusinessException {
        Example<ExamUser> example = Example.of(user);
        if (examUserDao.findOne(example).isEmpty()) {
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

    @Override
    public void verifyResetPasswordProcess(String email, String password) throws BusinessException {
        ExamUser examUser = new ExamUser();
        examUser.setEmail(email);
        Optional<ExamUser> examUserOptional = examUserDao.findOne(Example.of(examUser));
        if (examUserOptional.isEmpty()) {
            throw new BusinessException(ErrorEnum.ERROR_CANNOT_FIND_USER);
        }
        ExamUser afterUser = examUserOptional.get();
        String salt = RandomUtils.getRandomStringWithLowercase(64);
        afterUser.setPassword(passwordEncoder.encode(password + salt));
        afterUser.setSalt(salt);
        examUserDao.saveAndFlush(afterUser);
    }

    @Override
    public String resend(String token, Integer type) {
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


}
