package com.bazingaturk.data.repository;

import com.bazingaturk.data.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User findByUserId(String userId);
    String deleteByUserId(String userId);
}
