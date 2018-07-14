package com.bazingaturk.app;

import com.bazingaturk.business.service.UserSvc;
import com.bazingaturk.data.entity.user.User;
import com.bazingaturk.data.entity.user.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

@Controller
public class RedirectController {

    @Autowired
    private UserSvc userSvc;

    @RequestMapping(value = "/registrationConfirm/{token}", method = RequestMethod.GET)
    public String confirmRegistration(@PathVariable("token") String token) {
        VerificationToken verificationToken = userSvc.getVerificationToken(token);
        if (verificationToken == null) {
           return "redirect:/signFail";//重定向到错误的界面
        }
        User user = userSvc.findUserByUserId(verificationToken.getUserId());
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "redirect:/#/signFail";//重定向到错误的界面
        }
        if(user.isEnabled())
            return "redirect:/#/signFail";//重定向到错误的界面
        user.setEnabled(true);
        userSvc.updateUser(user);
        userSvc.createUserDetailInfo(user);
        return "redirect:/#/signUpSuccess";//重定向到正确的界面
    }

    @RequestMapping(value = "/retrievePasswordConfirm/{token}", method = RequestMethod.GET)
    public String retrievePasswordConfirm(@PathVariable("token") String token, HttpServletResponse response) {
        VerificationToken verificationToken = userSvc.getVerificationToken(token);
        if (verificationToken == null) {
            return "redirect;/#/resetPassFail";//重定向到错误的界面
        }
        User user = userSvc.findUserByUserId(verificationToken.getUserId());
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "redirect;/#/resetPassFail";//重定向到错误的界面
        }
        try{
        response.sendRedirect("/#/resetPass?"+user.getUsername());
        }catch(IOException e){
            System.out.println("emmm");
        }
        return "redirect:/#/resetPass?username="+user.getUsername();//重定向到正确的界面or密码重置的界面
    }
}
