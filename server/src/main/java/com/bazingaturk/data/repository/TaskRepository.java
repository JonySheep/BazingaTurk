package com.bazingaturk.data.repository;

import com.bazingaturk.data.entity.tasks.Task;
import com.bazingaturk.util.enums.TaskStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByStatus(TaskStatusEnum status);
    List<Task> findByRequesterId(String requesterId);
}
