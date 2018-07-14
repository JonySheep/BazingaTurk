package com.bazingaturk.business;

import com.bazingaturk.business.service.UserSvc;
import com.bazingaturk.business.vo.userVO.UserInfoVO;
import com.bazingaturk.data.entity.user.*;
import com.bazingaturk.data.repository.*;
import com.bazingaturk.util.MD5Util;
import com.bazingaturk.util.enums.ResultMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserSvcImpl implements UserSvc {
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final RequesterRepository requesterRepository;
    private final UserInfoRepository infoRepository;
    private final VerificationTokenRepository tokenRepository;

    @Autowired
    public UserSvcImpl(UserRepository userRepository, WorkerRepository workerRepository, RequesterRepository requesterRepository, UserInfoRepository infoRepository, VerificationTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.workerRepository = workerRepository;
        this.requesterRepository = requesterRepository;
        this.infoRepository = infoRepository;
        this.tokenRepository = tokenRepository;
    }


    @Override
    public ResultMessageEnum success(String userId) {
        UserInfo user = infoRepository.findByUserId(userId);
        if (user == null) {
            return ResultMessageEnum.FAIL;
        } else {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String lastLoginDate = user.getLastLoginDate();
            Date today = new Date();
            user.setLastLoginDate(format.format(today));
            if(lastLoginDate==null || lastLoginDate.equals("")){
                user.setContinuousLoginDays(1);
            }else{
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                today = calendar.getTime();
                String lastDayStr = format.format(today);
                if (lastDayStr.equals(lastLoginDate)) {
                    user.setContinuousLoginDays(user.getContinuousLoginDays() + 1);
                } else {
                    user.setContinuousLoginDays(1);
                }
            }
            infoRepository.save(user);
            return ResultMessageEnum.SUCCESS;
        }
    }

    @Override
    public User signUp(String username, String password, UserRole role) {
        password = MD5Util.encode(password);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            User newUser = new User(UUID.randomUUID().toString(), username, password, role.getRole(), false);
            newUser = userRepository.save(newUser);
            return newUser;
        }else if(!user.isEnabled()){
            user.setRole(role.getRole());
            user.setPassword(password);
            user=userRepository.save(user);
            return user;
        }
        return null;
    }

    public ResultMessageEnum createUserDetailInfo(User user){
        UserInfo info = new UserInfo(user);
        infoRepository.save(info);
        if (user.getRole().equals("Worker")) {
            Worker worker = new Worker(user.getUserId());
            workerRepository.save(worker);
        } else {
            Requester requester = new Requester(user.getUserId());
            requesterRepository.save(requester);
        }
        return ResultMessageEnum.SUCCESS;
    }


    @Override
    public UserInfoVO getUserInfo(String userId) {
        return new UserInfoVO(infoRepository.findByUserId(userId));

    }

    @Override
    public ResultMessageEnum updateAvatar(String userId, String url) {
        UserInfo info = infoRepository.findByUserId(userId);
        info.setAvatar(url);
        infoRepository.save(info);
        return ResultMessageEnum.SUCCESS;
    }

    @Override
    public ResultMessageEnum updateUserInfo(String userId, String name, String publicEmail) {
        UserInfo info = infoRepository.findByUserId(userId);
        if (name.equals("") && publicEmail.equals("")) {
            return ResultMessageEnum.SUCCESS;
        }
        if (!name.equals("")) {
            info.setName(name);
        }
        if (!publicEmail.equals("")) {
            info.setPublicEmail(publicEmail);
        }
        infoRepository.save(info);
        return ResultMessageEnum.SUCCESS;
    }

    @Override
    public ResultMessageEnum updateAccountEmail(String userId, String newEmail) {
        User user = userRepository.findByUserId(userId);
        User sameEmail = userRepository.findByUsername(newEmail);
        if (sameEmail != null) {
            return ResultMessageEnum.FAIL;
        }
        user.setUsername(newEmail);
        UserInfo info = infoRepository.findByUserId(userId);
        info.setUsername(newEmail);
        userRepository.save(user);
        infoRepository.save(info);
        return ResultMessageEnum.SUCCESS;
    }

    @Override
    public ResultMessageEnum updateAccountPass(String userId, String password) {
        password = MD5Util.encode(password);
        User user = userRepository.findByUserId(userId);
        user.setPassword(password);
        userRepository.save(user);
        return ResultMessageEnum.SUCCESS;
    }

    @Override
    public ResultMessageEnum removeAccount(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user.getRole().equals("Worker"))
            workerRepository.deleteByUserId(userId);
        else {
            requesterRepository.deleteByUserId(userId);
        }
        userRepository.deleteByUserId(userId);
        infoRepository.deleteByUserId(userId);
        return ResultMessageEnum.SUCCESS;
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void createVerificationTokenForUser(final String userId, final String token) {
        VerificationToken old = tokenRepository.findByUserId(userId);
        final VerificationToken myToken = new VerificationToken(token, userId);
        if (old == null) {
            tokenRepository.save(myToken);
        } else {
            old.setExpiryDate(myToken.getExpiryDate());
            old.setToken(myToken.getToken());
            old.setUserId(userId);
            tokenRepository.save(old);
        }
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public User findUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

}