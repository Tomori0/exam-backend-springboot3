package com.up9e.aichat.config;

import com.up9e.aichat.dao.UserDao;
import com.up9e.aichat.global.UserDetails;
import com.up9e.aichat.service.impl.UserDetailsServiceImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AIChatAuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public AIChatAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String presentedPassword = (String) authentication.getCredentials();
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("登录名或密码错误");
        }
        UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(email);
        if (userDetails == null) {
            throw new BadCredentialsException("用户名不存在");
        } else if (!this.passwordEncoder.matches(presentedPassword + userDetails.getSalt(), userDetails.getPassword())) {
            throw new BadCredentialsException("登录名或密码错误");
        } else {
            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
            result.setDetails(authentication.getDetails());
            return result;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}