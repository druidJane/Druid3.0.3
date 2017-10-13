package com.xuanwu.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangz on 2017/8/24.
 */
@Service
public class CustomUserService implements UserDetailsService{
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*角色级别
         List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User("admin",
                "123456", authorities);*/
        //权限级别
        List<GrantedAuthority> grantedAuthorities = new ArrayList <>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("PER_111");
        grantedAuthorities.add(grantedAuthority);
        return new User("admin",
                "123456", grantedAuthorities);
    }
}
