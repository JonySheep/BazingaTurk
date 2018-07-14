package com.bazingaturk.data.repository;

import com.bazingaturk.data.entity.user.UserInfo;
import com.bazingaturk.data.entity.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    UserInfo findByUserId(String userId);
    List<UserInfo> findByRole(String role);
    String deleteByUserId(String userId);
}
