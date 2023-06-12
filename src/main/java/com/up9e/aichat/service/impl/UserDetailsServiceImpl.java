package com.up9e.aichat.service.impl;

import com.up9e.aichat.constant.Constant;
import com.up9e.aichat.dao.AIChatUserLevelDao;
import com.up9e.aichat.dao.UserDao;
import com.up9e.aichat.entity.AIChatUserLevel;
import com.up9e.aichat.entity.User;
import com.up9e.aichat.global.UserDetails;
import org.springframework.data.domain.Example;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;
    private final AIChatUserLevelDao aiChatUserLevelDao;

    public UserDetailsServiceImpl(UserDao userDao, AIChatUserLevelDao aiChatUserLevelDao) {
        this.userDao = userDao;
        this.aiChatUserLevelDao = aiChatUserLevelDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取User信息
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

        // 获取UserLevel信息
        AIChatUserLevel findAIChatUserLevel = new AIChatUserLevel();
        findAIChatUserLevel.setUserId(returnUser.getId());
        findAIChatUserLevel.setDeleteFlg(Constant.STR_DELETEFLG_0);
        Optional<AIChatUserLevel> foundAIChatUserLevel = aiChatUserLevelDao.findOne(Example.of(findAIChatUserLevel));
        if (foundAIChatUserLevel.isEmpty()) {
            return null;
        }
        userDetails.setLevel(foundAIChatUserLevel.get().getLevel());
        userDetails.setExpireDate(foundAIChatUserLevel.get().getExpireDate());
        return userDetails;
    }
}
