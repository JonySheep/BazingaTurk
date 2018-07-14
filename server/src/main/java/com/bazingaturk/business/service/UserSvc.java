package com.bazingaturk.business.service;

import com.bazingaturk.business.vo.userVO.UserInfoVO;
import com.bazingaturk.data.entity.user.User;
import com.bazingaturk.data.entity.user.UserRole;
import com.bazingaturk.data.entity.user.VerificationToken;
import com.bazingaturk.util.enums.ResultMessageEnum;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserSvc {

    /**
     * 用户登录成功
     * @param userId
     * @return
     */
    ResultMessageEnum success(String userId);

    /**
     * 用户注册
     * @param username
     * @param password
     * @param role
     * @return
     */
    User signUp(String username, String password, UserRole role) ;


    /**
     * 获取指定用户的信息
     * @param userId
     * @return
     */
    UserInfoVO getUserInfo(String userId) ;

    /**
     * 更改用户头像
     * @param userId
     * @param url
     * @return
     */
    ResultMessageEnum updateAvatar(String userId,String url);

    /**
     * 更改用户昵称，公开邮箱
     * @param userId
     * @param name
     * @param publicEmail
     * @return
     */
    ResultMessageEnum updateUserInfo(String userId,String name,String publicEmail);

    /**
     * 更改用户账户邮箱
     * @param userId
     * @param newEmail
     * @return
     */
    ResultMessageEnum updateAccountEmail(String userId,String newEmail);

    /**
     * 更改用户密码
     * @param userId
     * @param password
     * @return
     */
    ResultMessageEnum updateAccountPass(String userId,String password);

    /**
     * 注销用户
     * @param userId
     * @return
     */
    ResultMessageEnum removeAccount(String userId);

    User findUserByUsername(String username);

    void createVerificationTokenForUser(String userId, String token);

    VerificationToken getVerificationToken(String token);

    User findUserByUserId(String userId);

    User updateUser(User user);

    ResultMessageEnum createUserDetailInfo(User user);

    }
