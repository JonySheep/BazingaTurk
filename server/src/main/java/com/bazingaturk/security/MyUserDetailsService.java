package com.bazingaturk.security;

import com.bazingaturk.business.service.UserSvc;
import com.bazingaturk.data.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserSvc userSvc;
//    @Autowired
//    MyUserDetailsService(UserSvc userSvc){
//        this.userSvc=userSvc;
//    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            user = userSvc.findUserByUsername(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("user not found");
        }
        if(user == null || user.isEnabled()==false){
            throw new UsernameNotFoundException("user not found");
        } else {
            return new MyUserDetails(user);
        }
    }
}

