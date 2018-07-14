package com.bazingaturk.data.repository;

import com.bazingaturk.data.entity.tasks.Partition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartitionRepository extends JpaRepository<Partition, String> {
}
