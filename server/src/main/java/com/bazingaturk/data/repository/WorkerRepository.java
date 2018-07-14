package com.bazingaturk.data.repository;

import com.bazingaturk.data.entity.user.User;
import com.bazingaturk.data.entity.user.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface WorkerRepository extends JpaRepository<Worker,Long> {
    Worker findByUserId(String userId);
    String deleteByUserId(String userId);
}
