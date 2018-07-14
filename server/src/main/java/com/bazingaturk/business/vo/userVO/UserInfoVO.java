package com.bazingaturk.business.vo.userVO;

import com.bazingaturk.data.entity.user.User;
import com.bazingaturk.data.entity.user.UserInfo;
import com.bazingaturk.data.entity.user.UserRole;
import com.bazingaturk.data.repository.UserRepository;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {

    private String userId;
    private String username;
    private String name;
    private String role;
    private String avatar="/server/users/Avatar/default.jpg";;
    private String publicEmail;
    private String signUpDate;
    private int continuousLoginDays;
    private String lastLoginDate;



    public UserInfoVO(UserInfo user){
        userId=user.getUserId();
        username=user.getUsername();
        name=user.getName();
        role=user.getRole();
        avatar=user.getAvatar();
        publicEmail=user.getPublicEmail();
        signUpDate=user.getSignUpDate();
        continuousLoginDays=user.getContinuousLoginDays();
        lastLoginDate=user.getLastLoginDate();

    }
}
