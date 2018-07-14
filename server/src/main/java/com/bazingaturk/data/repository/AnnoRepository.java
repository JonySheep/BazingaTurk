package com.bazingaturk.data.repository;

import com.bazingaturk.data.entity.anno.Anno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnoRepository extends JpaRepository<Anno, String> {
    Anno findByAssignmentIdAndImageId(String assignmentId,int imageId);
}
