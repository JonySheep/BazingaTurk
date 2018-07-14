package com.bazingaturk.app;

import com.bazingaturk.business.service.UserSvc;
import com.bazingaturk.data.entity.user.User;
import com.bazingaturk.data.entity.user.UserRole;
import com.bazingaturk.data.entity.user.VerificationToken;
import com.bazingaturk.security.MyUserDetails;

import com.bazingaturk.security.event.OnRegistrationCompleteEvent;
import com.bazingaturk.util.enums.ResultMessageEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class LoginController {

    @Autowired
    private UserSvc userSvc;

    @Autowired
    @SuppressWarnings("all")
    private MessageSource messages;

    @Autowired
    private ApplicationEventPublisher eventPublisher;


    @RequestMapping(value = "/user/success", method = GET)
    public Object loginSuccess() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        Map<String, Object> map = new HashMap<>();
        try {
            userSvc.success(user.getUserId());
            map.put("data", user.getUserId());
            map.put("success", true);
        } catch (Exception e) {
            map.put("success", false);
            map.put("data", null);
        }
        return map;
    }

    @RequestMapping("/login_p")
    public Object login(HttpServletResponse resp) throws IOException {

        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("data", "login");
        return map;
    }


    @RequestMapping(value = "/signUp", method = POST)
    public Object registerUserAccount(@RequestBody Map<String,String> param, final HttpServletRequest request) {
        Map<String,Object> map=new HashMap<>();
        final User registered = userSvc.signUp(param.get("username"), param.get("password"), new UserRole(param.get("role")));
        if(registered==null){
            map.put("success",true);
            map.put("data",ResultMessageEnum.FAIL);
            return map;
        }
        String appUrl="http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
        map.put("success",true);
        map.put("data",ResultMessageEnum.SUCCESS);
        return map;
    }



}