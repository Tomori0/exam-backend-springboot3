package com.up9e.aichat.global;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
public class UserDetails extends User {

    private Integer id;
    private String email;
    private String nickname;
    private Date birthday;
    private String password;
    private String avatar;
    private String salt;
    private Timestamp lastLoginTime;
    private Timestamp createTime;

    public UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username;
        this.password = password;
    }

}
