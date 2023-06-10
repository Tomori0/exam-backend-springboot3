package com.up9e.aichat.service.impl;

import com.up9e.aichat.dao.UserDao;
import com.up9e.aichat.entity.User;
import com.up9e.aichat.global.UserDetails;
import org.springframework.data.domain.Example;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User();
        user.setEmail(username);
        Example<User> userExample = Example.of(user);
        Optional<User> userOptional = userDao.findOne(userExample);
        if (userOptional.isEmpty()) {
            return null;
        }
        User returnUser = userOptional.get();
        UserDetails userDetails = new UserDetails(returnUser.getEmail(), returnUser.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("USER"));
        userDetails.setAvatar(returnUser.getAvatar());
        userDetails.setBirthday(returnUser.getBirthday());
        userDetails.setNickname(returnUser.getNickname());
        userDetails.setCreateTime(returnUser.getCreateTime());
        userDetails.setLastLoginTime(returnUser.getLastLoginTime());
        return userDetails;
    }
}
