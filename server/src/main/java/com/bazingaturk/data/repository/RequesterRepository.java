package com.bazingaturk.data.repository;

import com.bazingaturk.data.entity.user.Requester;
import com.bazingaturk.data.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface RequesterRepository extends JpaRepository<Requester,Long> {
    Requester findByUserId(String userId);
    String deleteByUserId(String userId);
}
