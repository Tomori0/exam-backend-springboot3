package com.up9e.exam.config;

import com.up9e.exam.dao.ExamUserDao;
import com.up9e.exam.entity.ExamUser;
import com.up9e.exam.global.ExamUserDetails;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ExamAuthenticationProvider implements AuthenticationProvider {

    private final ExamUserDao examUserDao;

    private final PasswordEncoder passwordEncoder;

    public ExamAuthenticationProvider(ExamUserDao examUserDao, PasswordEncoder passwordEncoder) {
        this.examUserDao = examUserDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String presentedPassword = (String) authentication.getCredentials();
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("登录名或密码错误");
        }
        ExamUser examUserForExample = new ExamUser();
        examUserForExample.setEmail(email);
        Example<ExamUser> examUserExample = Example.of(examUserForExample);
        Optional<ExamUser> examUserOptional = examUserDao.findOne(examUserExample);
        if (examUserOptional.isEmpty()) {
            throw new BadCredentialsException("用户名不存在");
        } else {
            ExamUser examUser = examUserOptional.get();
            ExamUserDetails examUserDetails = new ExamUserDetails(email, examUser.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("USER"));
            examUserDetails.setId(examUser.getId());
            examUserDetails.setBirthday(examUser.getBirthday());
            examUserDetails.setAvatar(examUser.getAvatar());
            examUserDetails.setNickname(examUser.getNickname());
            examUserDetails.setCreateTime(examUser.getCreateTime());
            examUserDetails.setLastLoginTime(examUser.getLastLoginTime());
            if (!this.passwordEncoder.matches(presentedPassword + examUser.getSalt(), examUser.getPassword())) {
                throw new BadCredentialsException("登录名或密码错误");
            } else {
                UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(examUserDetails, authentication.getCredentials(), examUserDetails.getAuthorities());
                result.setDetails(authentication.getDetails());
                return result;
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
