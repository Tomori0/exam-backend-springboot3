package com.up9e.exam.service.impl;

import com.up9e.exam.dao.ExamUserDao;
import com.up9e.exam.entity.ExamUser;
import com.up9e.exam.global.ExamUserDetails;
import org.springframework.data.domain.Example;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExamUserDetailsServiceImpl implements UserDetailsService {

    private final ExamUserDao examUserDao;

    private final PasswordEncoder passwordEncoder;

    public ExamUserDetailsServiceImpl(ExamUserDao examUserDao, PasswordEncoder passwordEncoder) {
        this.examUserDao = examUserDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ExamUser examUser = new ExamUser();
        examUser.setEmail(username);
        Example<ExamUser> examUserExample = Example.of(examUser);
        Optional<ExamUser> examUserOptional = examUserDao.findOne(examUserExample);
        if (examUserOptional.isEmpty()) {
            return null;
        }
        ExamUser returnUser = examUserOptional.get();
        ExamUserDetails userDetails = new ExamUserDetails(returnUser.getEmail(), returnUser.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("USER"));
        userDetails.setAvatar(returnUser.getAvatar());
        userDetails.setBirthday(returnUser.getBirthday());
        userDetails.setNickname(returnUser.getNickname());
        userDetails.setCreateTime(returnUser.getCreateTime());
        userDetails.setLastLoginTime(returnUser.getLastLoginTime());
        return userDetails;
    }
}
