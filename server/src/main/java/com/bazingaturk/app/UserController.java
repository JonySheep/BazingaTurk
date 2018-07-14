package com.bazingaturk.app;

import com.bazingaturk.WebAppConfig;
import com.bazingaturk.business.service.UserSvc;
import com.bazingaturk.data.entity.user.User;
import com.bazingaturk.security.MyUserDetails;
import com.bazingaturk.security.event.ForgotPasswordEvent;
import com.bazingaturk.util.enums.ResultMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserSvc userSvc;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @SuppressWarnings("all")
    @Autowired
    private MessageSource messages;


    @RequestMapping(value = "/{user_id}", method = GET)
    public Object getUserInfo(@PathVariable("user_id") String userId) {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("data", userSvc.getUserInfo(userId));
            map.put("success", true);
        } catch (Exception e) {
            map.put("data", e.getStackTrace());
            map.put("success", false);
        }
        return map;
    }

    @RequestMapping(value = "/current", method = GET)
    public Object getCurrentUser() {
        Map<String, Object> map = new HashMap<>();
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken){
            map.put("data", null);
            map.put("success",true);
        }
        else {
            System.out.println(auth.isAuthenticated());
            MyUserDetails user = (MyUserDetails) auth.getPrincipal();
            map.put("data", user.getUserId());
        }
        map.put("success", true);
        return map;
    }

    @RequestMapping(value = "/{user_id}/avatar", method = POST)
    public Object updateAvatar(@RequestParam(value = "avatar") MultipartFile avatar,
                               @PathVariable(value = "user_id") String userId) {
        Map<String, Object> map = new HashMap<>();

        String imgPath = WebAppConfig.BASE + "/assets/users/Avatar/" + userId + ".jpg";

        System.out.println(imgPath);
        File newFile = new File(imgPath);
        if (newFile.exists()) {
            newFile.delete();
        }
        newFile = new File(imgPath);
        try {
            newFile.createNewFile();
            avatar.transferTo(new File(newFile.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
            map.put("data", e.getStackTrace());
            map.put("success", false);
        }

        try {
            String s = "/server/users/Avatar/" + userId + ".jpg";
            ResultMessageEnum reault = userSvc.updateAvatar(userId, s);
            if (reault.equals(ResultMessageEnum.SUCCESS)) {
                map.put("data", s);
                map.put("success", true);
            } else {
                map.put("data", null);
                map.put("success", false);
            }
        } catch (Exception e) {
            map.put("data", e.getStackTrace());
            map.put("success", false);
        }
        return map;
    }

    @RequestMapping(value = "/{user_id}", method = POST)
    public Object updateUserInfo(@PathVariable("user_id") String userId,
                                 @RequestParam("public_email") String email,
                                 @RequestParam("name") String name) {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("data", userSvc.updateUserInfo(userId, name, email));
            map.put("success", true);
        } catch (Exception e) {
            map.put("data", e.getStackTrace());
            map.put("success", false);
        }
        return map;
    }

    @RequestMapping(value = "/{user_id}/accountEmail", method = POST)
    public Object updateAccountEmail(@PathVariable("user_id") String userId,
                                     @RequestParam("email") String email) {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("data", userSvc.updateAccountEmail(userId, email));
            map.put("success", true);
        } catch (Exception e) {
            map.put("data", e.getStackTrace());
            map.put("success", false);
        }
        return map;
    }

    @RequestMapping(value = "/{user_id}/password", method = POST)
    public Object updateAccountPass(@PathVariable("user_id") String userId,
                                    @RequestParam("password") String password) {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("data", userSvc.updateAccountPass(userId, password));
            map.put("success", true);
        } catch (Exception e) {
            map.put("data", e.getStackTrace());
            map.put("success", false);
        }
        return map;
    }

    @RequestMapping(value = "/{user_id}/remove", method = GET)
    public Object removeAccount(@PathVariable("user_id") String userId) {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("data", userSvc.removeAccount(userId));
            map.put("success", true);
        } catch (Exception e) {
            map.put("data", e.getStackTrace());
            map.put("success", false);
        }
        return map;
    }

    @RequestMapping(value = "/retrievePassword/{username}", method = GET)
    public Object retrievePassword(@PathVariable("username") String username, final HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        User user = userSvc.findUserByUsername(username);
        if (user == null || !user.isEnabled()) {
            map.put("success", true);
            map.put("data", ResultMessageEnum.FAIL);
            return map;
        }
        eventPublisher.publishEvent(new ForgotPasswordEvent(user, request.getLocale(), appUrl));
        map.put("success", true);
        map.put("data", ResultMessageEnum.SUCCESS);
        return map;
    }


    @RequestMapping(value = "/resetPassword", method = POST)
    public Object resetPassword(@RequestBody Map<String, String> param) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", userSvc.updateAccountPass(userSvc.findUserByUsername(param.get("username")).getUserId(), param.get("password")));
        map.put("success", true);
        return map;

    }


}