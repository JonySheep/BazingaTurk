package com.bazingaturk.data.entity.user;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
public class UserInfo {
    @Id
    @GenericGenerator(name="system-uuid",strategy="uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(length = 32)
    private String id;

    private String userId;
    private String username;
    private String name;
    private String role;
    private String avatar;
    private String publicEmail;
    private String signUpDate;
    private int continuousLoginDays;
    private String lastLoginDate;


    public UserInfo(){
        id=null;
        username=null;
        name=null;
        role=null;
        publicEmail=null;
        avatar="/server/users/Avatar/default.jpg";
        userId=null;
        signUpDate=null;
        continuousLoginDays=0;
        lastLoginDate=null;
    }

    public UserInfo(User user){
        username=user.getUsername();
        role=user.getRole();
        publicEmail=user.getUsername();
        name=user.getUsername();
        avatar="/server/users/Avatar/default.jpg";
        userId=user.getUserId();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = formatter.format(new Date());
        signUpDate=dateStr;
        continuousLoginDays=0;
        lastLoginDate=null;
        userId=user.getUserId();
        id=null;
    }

}
