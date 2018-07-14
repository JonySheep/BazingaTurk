package com.bazingaturk.security.event;

import com.bazingaturk.business.service.UserSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ForgotPasswordListener implements ApplicationListener<ForgotPasswordEvent> {
    @Autowired
    private UserSvc userSvc;
    @SuppressWarnings("all")
    @Autowired
    private MessageSource messages;
    @SuppressWarnings("all")
    @Autowired
    private JavaMailSender mailSender;

    // API

    @Override
    public void onApplicationEvent(final ForgotPasswordEvent event) {
        this.findPassword(event);
    }

    private void findPassword(final ForgotPasswordEvent event) {
        final String userId = event.getUser().getUserId();
        final String token = UUID.randomUUID().toString();
        userSvc.createVerificationTokenForUser(userId, token);
        final SimpleMailMessage email = constructEmailMessage(event, userId, token);
        mailSender.send(email);
    }


    private final SimpleMailMessage constructEmailMessage(final ForgotPasswordEvent event, final String userId, final String token) {
        final String recipientAddress = userSvc.getUserInfo(userId).getUsername();
        final String subject = "retrievePassword Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/retrievePasswordConfirm/"+ token;
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(" \r\n" + confirmationUrl);
        email.setFrom("m15351314887@163.com");
        return email;
    }

}


