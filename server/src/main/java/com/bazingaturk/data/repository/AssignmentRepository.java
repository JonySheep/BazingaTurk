package com.bazingaturk.data.repository;

import com.bazingaturk.data.entity.tasks.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, String> {
    List<Assignment> findByWorkerId(String workerId);
}
