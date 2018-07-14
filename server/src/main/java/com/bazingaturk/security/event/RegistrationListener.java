package com.bazingaturk.security.event;

import java.util.UUID;

import com.bazingaturk.business.service.UserSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
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
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        final String userId = event.getUser().getUserId();
        final String token = UUID.randomUUID().toString();
        userSvc.createVerificationTokenForUser(userId, token);
        final SimpleMailMessage email = constructEmailMessage(event, userId, token);
        mailSender.send(email);
    }

    //

    private final SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final String userId, final String token) {
        final String recipientAddress = userSvc.findUserByUserId(userId).getUsername();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm/"+ token;
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(" \r\n" + confirmationUrl);
        email.setFrom("m15351314887@163.com");
        return email;
    }

}

